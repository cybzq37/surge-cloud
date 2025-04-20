package com.surge.map.api;

import com.surge.map.domain.entity.DataEntrySet;

import java.util.List;

public interface RemoteDataEntryService {

    void batchUpdateDataEntrySet(String payload, String field);

    void saveOrUpdate(DataEntrySet dataEntrySet);

    DataEntrySet queryOne(Long entryId, String field, String value);

    void updateOne(String payload);
}
