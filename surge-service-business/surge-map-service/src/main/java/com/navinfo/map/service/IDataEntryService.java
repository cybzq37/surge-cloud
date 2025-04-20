package com.surge.map.service;

import com.surge.common.core.utils.tree.TreeNode;
import com.surge.map.domain.entity.DataEntry;
import com.surge.map.domain.dto.DataEntryUploadDTO;

import java.util.List;

public interface IDataEntryService {

    void upload(DataEntryUploadDTO dataEntryUploadDTO);

    DataEntry queryById(Long dataEntryId);

    List<TreeNode> queryTreeList();

    void updateOne(DataEntry dataEntry);

    void removeById(Long dataEntryId);

    void create(DataEntry dataEntry);

    void update(DataEntry dataEntry);

}