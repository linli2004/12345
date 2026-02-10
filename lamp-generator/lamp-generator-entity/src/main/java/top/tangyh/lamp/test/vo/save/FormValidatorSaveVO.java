package top.tangyh.lamp.test.vo.save;

import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import top.tangyh.basic.annotation.constraints.NotEmptyPattern;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static top.tangyh.basic.utils.ValidatorUtil.REGEX_MOBILE;

/**
 * <p>
 * 表单保存方法VO
 * 测试树结构
 * </p>
 *
 * @author zuihou
 * @date 2022-04-20 00:28:30
 */
@Data
public class FormValidatorSaveVO implements Serializable {

    @AssertFalse(message = "必须是false")
    private boolean assertFalse2;
    @AssertTrue(message = "必须是true")
    private boolean assertTrue2;

    @NotEmpty(message = "请填写名称")
    @Size(max = 24, message = "名称长度不能超过{max}")
    private String name;

    @NotEmpty(message = "请填写名称2")
    @Size(min = 4, max = 24, message = "名称长度必须介于{min}-{max}之间")
    private String name2;

    @NotBlank(message = "不能为空")
    private String notBlank;
    @NotNull(message = "不能为空")
    private Integer notNull;
    @NotNull(message = "不能为空")
    private Long notNullLong;

    @Null(message = "必须为空")
    private Integer null2;


    @DecimalMax(value = "123.45", message = "不能大于{value}")
    private BigDecimal decimalMax;
    @DecimalMin(value = "444.1", message = "不能小于{value}")
    private BigDecimal decimalMin;
    @Digits(integer = 4, fraction = 2, message = "整数部分不能超过{integer}，小数部分不能超过{fraction}")
    private BigDecimal digits;

    @Max(value = 12, message = "不能超过{value}")
    private BigDecimal max;
    @Min(value = 2, message = "不能小于{value}")
    private BigDecimal min;

    @Email(message = "请输入正确的邮箱")
    private String email;

    @Future(message = "必须大于当前时间")
    private LocalDateTime futureDateTime;
    @Future(message = "必须大于当前时间")
    private LocalDate futureLocalDate;

    @Future(message = "必须大于当前时间")
    private Date futureDate;
    @FutureOrPresent(message = "必须大于等于当前时间")
    private LocalDateTime futureOrPresentDateTime;

    @Past(message = "必须小于当前时间")
    private LocalDateTime pastDateTime;
    @PastOrPresent(message = "必须小于等于当前时间")
    private LocalDateTime pastOrPresentDateTime;


    @Negative(message = "必须小于0")
    private Long negative;
    @NegativeOrZero(message = "必须小于等于0")
    private Long negativeOrZero;
    @Positive(message = "必须大于0")
    private Long positive;
    @PositiveOrZero(message = "必须大于等于0")
    private Long positiveOrZero;


    @Size(max = 11, message = "登录手机号长度不能超过{max}")
    @NotEmptyPattern(regexp = REGEX_MOBILE, message = "请输入11位合法的手机号")
    @NotEmpty(message = "请填写登录手机号")
    private String mobile;


    @Pattern(regexp = REGEX_MOBILE, message = "请输入11位合法的手机号")
    private String mobile2;

}
