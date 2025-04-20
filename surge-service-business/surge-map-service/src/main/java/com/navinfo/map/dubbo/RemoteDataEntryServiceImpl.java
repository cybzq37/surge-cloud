package com.surge.map.dubbo;

import com.surge.common.core.utils.JsonUtils;
import com.surge.common.core.utils.StringUtils;
import com.surge.map.api.RemoteDataEntryService;
import com.surge.map.domain.entity.DataEntrySet;
import com.surge.map.repository.DataEntryRepository;
import com.surge.map.repository.DataEntrySetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@DubboService
@RequiredArgsConstructor
public class RemoteDataEntryServiceImpl implements RemoteDataEntryService {

    private final DataEntryRepository dataEntryRepository;
    private final DataEntrySetRepository dataEntrySetRepository;

    @Override
    public void batchUpdateDataEntrySet(String payload, String field) {
        List<DataEntrySet> dataEntrySets = JsonUtils.parseArray(payload, DataEntrySet.class);
        for (DataEntrySet dataEntrySet : dataEntrySets) {
            Map<String, String> fieldInfo = new HashMap<>();
            if(StringUtils.isNotEmpty(field)) {
                fieldInfo.put(field, dataEntrySet.getFieldInfo().get(field).textValue());
            }
            DataEntrySet set = dataEntrySetRepository.getBaseMapper().selectOne(dataEntrySet.getDataEntryId(), fieldInfo);
            if(set != null) {
                dataEntrySetRepository.updateById(set);
            } else {
                dataEntrySetRepository.save(dataEntrySet);
            }
        }
    }

    @Override
    public void saveOrUpdate(DataEntrySet dataEntrySet) {
        if(dataEntrySet == null) {
            return;
        }
        if(dataEntrySet.getId()!= null) {
            dataEntrySetRepository.getBaseMapper().updateById(dataEntrySet);
        }else{
           dataEntrySetRepository.getBaseMapper().insert(dataEntrySet);
        }
    }

    @Override
    public DataEntrySet queryOne(Long entryId, String field, String value) {
        Map<String, String> fieldInfo = new HashMap<>();
        if(StringUtils.isNotEmpty(field)) {
            fieldInfo.put(field, value);
        }
        DataEntrySet set = dataEntrySetRepository.getBaseMapper().selectOne(entryId, fieldInfo);
        return set;
    }

    @Override
    public void updateOne(String payload) {
        DataEntrySet dataEntrySet = JsonUtils.parseObject(payload, DataEntrySet.class);
        dataEntrySetRepository.getBaseMapper().updateById(dataEntrySet);
    }

    public static void main(String[] args) {
        String str = "{\"id\":\"1895365398442811393\",\"dataEntryId\":\"1892765406222471170\",\"geoJson\":null,\"geom\":null,\"fieldInfo\":{\"bayQty\":38,\"rowQty\":4,\"blockNo\":\"QC1\",\"tierQty\":1,\"lastBayNo\":\"75\",\"lastRowNo\":\"04\",\"firstBayNo\":\"01\",\"firstRowNo\":\"01\",\"lastTierNo\":\"01\",\"firstTierNo\":\"01\",\"bays\":[{\"bayNo\":2,\"rows\":[{\"rowNo\":1,\"tierSize\":1,\"tiers\":[{\"tierNo\":1,\"unitVisitId\":\"EE69EB28E7C2579DE0530439A8C0BA71\",\"unitVisitNo\":\"221127300117\",\"unitNo\":\"TEMU7321409\"}]},{\"rowNo\":2,\"tierSize\":1,\"tiers\":[{\"tierNo\":1,\"unitVisitId\":\"F15673370CEDD4FFE0530439A8C073B8\",\"unitVisitNo\":\"230103302890\",\"unitNo\":\"TCNU7062308\"}]},{\"rowNo\":3,\"tierSize\":1,\"tiers\":[{\"tierNo\":1,\"unitVisitId\":\"EFE871960E6EAFE6E0530439A8C02FF8\",\"unitVisitNo\":\"221216300496\",\"unitNo\":\"CAIU8722608\"}]}]},{\"bayNo\":43,\"rows\":[{\"rowNo\":1,\"tierSize\":1,\"tiers\":[{\"tierNo\":1,\"unitVisitId\":\"8C7FA48350BA69B8E0530439A8C0CEB5\",\"unitVisitNo\":\"181211150298\",\"unitNo\":\"MHSU2256826\"}]},{\"rowNo\":2,\"tierSize\":1,\"tiers\":[{\"tierNo\":1,\"unitVisitId\":\"8C7FA4834F4469B8E0530439A8C0CEB5\",\"unitVisitNo\":\"181206080100\",\"unitNo\":\"GLDU3714770\"}]}]},{\"bayNo\":75,\"rows\":[{\"rowNo\":1,\"tierSize\":1,\"tiers\":[{\"tierNo\":1,\"unitVisitId\":\"8C7FA483540269B8E0530439A8C0CEB5\",\"unitVisitNo\":\"161017150010\",\"unitNo\":\"SEGU1643377\"}]},{\"rowNo\":2,\"tierSize\":1,\"tiers\":[{\"tierNo\":1,\"unitVisitId\":\"8C7FA483504969B8E0530439A8C0CEB5\",\"unitVisitNo\":\"240425300331\",\"unitNo\":\"TEMU2474832\"}]}]}]}}";
        DataEntrySet set = JsonUtils.parseObject(str, DataEntrySet.class);
        System.out.println(set);
    }


}
