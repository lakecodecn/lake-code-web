<script setup>
import {PlusOutlined, DeleteOutlined, DownOutlined} from '@ant-design/icons-vue';
import {reactive, ref, onMounted} from 'vue';
import {
  ${moduleName}_add,
  ${moduleName}_edit,
  ${moduleName}_del,
  ${moduleName}_list
} from '../../api/${moduleName}'

const formRef = ref()
const searchFormRef = ref()

const state = reactive({
  searchForm: {
    name: '',
    key: ''
  },
  form: {
    name: '',
    key: ''
  },
  list: [],
  total: 0,
  current: 1,
  pageSize: 20,
  selectedRowKeys: [],
  dialog: false,
  edit: false,
  editData: {},
  tableLoading: false,
  dialogLoading: false,
})

const columns = [
  {title: '编号', dataIndex: 'id'},
  {title: '名称', dataIndex: 'name'},
  {title: 'key', dataIndex: 'key'},
  {title: '操作', dataIndex: 'operate'}
]

const rules = {
  name: [{
    required: true,
    message: '请输入名称',
    trigger: 'blur',
  },],
  key: [{
    required: true,
    message: '请输入key',
    trigger: 'blur',
  }]
}

// 批量操作
const batchOperate = (e) => {
  if (e.key === 'del') {
    if (state.selectedRowKeys) {
      let str = state.selectedRowKeys.join(",")
      submitDel(str)
    }
  }
}

// 表格筛选
const onSelectChange = selectedRowKeys => {
  state.selectedRowKeys = selectedRowKeys;
};


// 分页变化
const pageChange = (pagination, filters, sorter, {action, currentDataSource}) => {
  if (action === 'pagination') {
    state.current = pagination.current
    state.pageSize = pagination.pageSize
    getTableData()
  }
}

// 对话框点击确定
const handleDialogOk = () => {
  if (!state.edit) {
    state.dialogLoading = true
    formRef.value.validate().then(() => {
      ${moduleName}_add(state.form).then(res => {
        state.dialogLoading = false
        if (res.errCode === 0) {
          state.dialog = false
          resetForm()
          initData()
        }
      })
    })
  } else {
    formRef.value.validate().then(() => {
      state.dialogLoading = true
      ${moduleName}_edit({
        id: state.editData.id,
        name: state.form.name,
        key: state.form.key
      }).then(res => {
        state.dialogLoading = false
        if (res.errCode === 0) {
          state.dialog = false
          resetForm()
          initData()
        }
      })
    })
  }
}

// 打开对话框
const openDialog = (f, record) => {
  state.edit = f
  state.dialog = true
  if (f) {
    state.editData = record;
    state.form.key = record.key
    state.form.name = record.name
  }
}

// 重置表单
const resetForm = () => {
  formRef.value.resetFields();
}

// 重置表单
const resetSearchForm = () => {
  searchFormRef.value.resetFields();
}

// 初始化数据,从第一页开始
const initData = () => {
  state.current = 1
  getTableData()
}

// 获取表格数据
const getTableData = () => {
  state.tableLoading = true
  ${moduleName}_list({
    pageSize: state.pageSize,
    currentPage: state.current,
    name: state.searchForm.name
  }).then(res => {
    state.tableLoading = false
    if (res.errCode === 0) {
      state.list = res.data.list
      state.total = res.data.total
    }
  })
}

import {confirm} from '../../utils/util'

const submitDel = (id) => {
  confirm('确认删除该权限?').then(() => {
    state.dialogLoading = true
    ${moduleName}_del({idsStr: id}).then(res => {
      state.dialogLoading = false
      if (res.errCode === 0) {
        initData()
      }
    })
  })
}

// 初始化调用
onMounted(() => {
  initData()
})


</script>
<template>
  <div>
    <div class="table-page-search-wrapper">
      <a-form ref="searchFormRef" layout="inline" :model="state.searchForm">
        <a-row :gutter="48">
          <a-col :md="8" :sm="24">
            <a-form-item label="名称" name="name">
              <a-input v-model:value="state.searchForm.name" placeholder="输入查询"/>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24">
                        <span class="table-page-search-submitButtons">
                            <a-button type="primary" @click="initData">
                                查询
                            </a-button>
                            <a-button style="margin-left: 8px;" @click="resetSearchForm">
                                重置
                            </a-button>
                        </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <div class="table-operator">
      <a-button type="primary" @click="openDialog(false)">
        <template #icon>
          <PlusOutlined/>
        </template>
        新增
      </a-button>
      <a-dropdown>
        <template #overlay>
          <a-menu @click="batchOperate">
            <a-menu-item key="del">
              <template #icon>
                <DeleteOutlined/>
              </template>
              删除
            </a-menu-item>
          </a-menu>
        </template>
        <a-button style="margin-left: 8px">
          <template #icon>
            <DownOutlined/>
          </template>
          批量操作
        </a-button>
      </a-dropdown>
    </div>

    <a-table :loading="state.tableLoading" :dataSource="state.list" :columns="columns" rowKey="id"
             :row-selection="{ selectedRowKeys: state.selectedRowKeys, onChange: onSelectChange }"
             :pagination="{ current: state.current, total: state.total, pageSize: state.pageSize }"
             @change="pageChange">
      <template #bodyCell="{ column, text, record }">
        <template v-if="column.dataIndex === 'operate'">
          <a-button type="link" @click="openDialog(true,record)">编辑</a-button>
          <a-button type="link" danger @click="submitDel(record.id)">删除</a-button>
        </template>
      </template>
    </a-table>

    <a-modal width="400px" cancelText="取消" okText="确认" v-model:open="state.dialog"
             :title="state.edit ? '编辑' : '新增'"
             @ok="handleDialogOk" :confirm-loading="state.dialogLoading">
      <a-form ref="formRef" :label-col="{ style: { width: '60px' } }" class="dialog-form" :rules="rules"
              :model="state.form">
        <a-form-item label="名称" name="name">
          <a-input v-model:value="state.form.name"/>
        </a-form-item>
        <a-form-item label="key" name="key">
          <a-input v-model:value="state.form.key"/>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>
