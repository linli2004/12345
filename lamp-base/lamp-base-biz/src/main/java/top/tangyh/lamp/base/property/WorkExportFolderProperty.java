package top.tangyh.lamp.base.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "work.export")
public class WorkExportFolderProperty {
    private String workFinishExcelPath;
    private String workFinishWordPath;
    private String workBackWordPath;
    private String workBackExcelPath;
    private String chiefWorkOrderTemplatePath;
}
