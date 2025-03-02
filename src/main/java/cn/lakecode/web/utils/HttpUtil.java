package cn.lakecode.web.utils;

import cn.lakecode.web.resp.R;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.Map;

public class HttpUtil {

    private final static RestTemplate restTemplate = new RestTemplate();


    public static String get(String url, Map<String, String> params) {
        url = buildParams(url, params);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }


    public static String postWithJson(String url, Map<String, String> headers, Map<String, String> data) {
        // 设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return post(url, headers, data, httpHeaders);
    }

    public static String post(String url, Map<String, String> headers, Map<String, String> data) {
        // 设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return post(url, headers, data, httpHeaders);
    }


    public static String upload(String url, InputStream stream) {
        InputStreamResource fileResource = new InputStreamResource(stream);
        // 创建表单数据
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("file", fileResource);

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 创建 HttpEntity 包含请求头和表单数据
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(formData, headers);

        // 发送 POST 请求
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        return response.getBody();
    }


    public static void download(String url, String path) {
        ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
        // 检查响应状态码
        if (response.getStatusCode().is2xxSuccessful()) {
            // 获取文件内容
            byte[] fileBytes = response.getBody();
            // 指定文件保存路径
            File file = new File("path/to/save/file.txt"); // 替换为你的保存路径

            try (OutputStream outputStream = new FileOutputStream(file)) {
                assert fileBytes != null;
                outputStream.write(fileBytes);
            } catch (Exception e) {
                throw new RuntimeException("文件下载失败", e);
            }
        }
        throw new RuntimeException("文件下载失败");
    }

    private static String post(String url, Map<String, String> headers, Map<String, String> data, HttpHeaders httpHeaders) {
        if (headers != null) {
            headers.forEach(httpHeaders::set);
        }
        // 创建 HttpEntity 包含请求头和请求体
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(data, httpHeaders);
        // 发送 POST 请求
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
        return response.getBody();
    }


    private static String buildParams(String url, Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return url;
        }
        StringBuilder sb = new StringBuilder();
        params.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
        return url + "?" + sb.substring(0, sb.length() - 1);
    }

    public static String getIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");

        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }

        // 如果有多个IP地址，取第一个
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0];
        }

        return ipAddress;
    }

    /**
     * 响应
     *
     * @param response HttpServletResponse
     * @param r        R
     * @throws IOException 异常
     */
    public static void writeR(HttpServletResponse response, R<?> r) throws IOException {
        // 设置响应内容类型为 JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // 创建 JSON 响应
        String jsonResponse = JSONObject.toJSONString(r);
        // 写入响应
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
}
