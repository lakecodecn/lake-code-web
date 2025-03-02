package ${packageName};

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


@Data
public class ${className} {

    @NotNull(message = "[id]不能为空", groups = Update.class)
    @Min(value = 1, message = "[id]格式错误", groups = Update.class)
    private Long id;

    /**
     * 标题
     */
    @NotNull(message = "[title]不能为空", groups = {Add.class})
    @Length(min = 2, max = 100, message = "[title]格式错误")
    private String title;


    public interface Add {
    }

    public interface Update {
    }
}
