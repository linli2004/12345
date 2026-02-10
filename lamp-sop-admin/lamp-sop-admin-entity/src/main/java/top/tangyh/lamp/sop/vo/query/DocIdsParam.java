package top.tangyh.lamp.sop.vo.query;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * @author zuihou
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocIdsParam {

    @NotNull
    private Collection<Long> docIds;


}
