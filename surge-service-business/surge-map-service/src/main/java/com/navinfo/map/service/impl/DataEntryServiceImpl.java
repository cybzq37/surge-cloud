package com.surge.map.service.impl;

import com.surge.common.core.utils.StringUtils;
import com.surge.common.core.utils.tree.TreeNode;
import com.surge.common.core.utils.tree.TreeUtils;
import com.surge.map.domain.entity.DataEntry;
import com.surge.map.domain.dto.DataEntryUploadDTO;
import com.surge.map.repository.DataEntryRepository;
import com.surge.map.repository.DataEntrySetRepository;
import com.surge.map.service.IDataEntryService;
import com.surge.system.api.RemoteOrgService;
import com.surge.system.domain.entity.SysOrg;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DataEntryServiceImpl implements IDataEntryService {

    private final DataEntryRepository dataEntryRepository;
    private final DataEntrySetRepository dataEntrySetRepository;
    @DubboReference
    private final RemoteOrgService remoteOrgService;

    @Override
    public void upload(DataEntryUploadDTO dataEntryUploadDTO) {

    }

    @Override
    public DataEntry queryById(Long dataEntryId) {
        return dataEntryRepository.getById(dataEntryId);
    }

    @Override
    public List<TreeNode> queryTreeList() {
        List<TreeNode<Long>> treeNodes = new ArrayList<>();
        List<SysOrg> sysOrgList = remoteOrgService.queryAll();
        for (SysOrg sysOrg : sysOrgList) {
            TreeNode<Long> treeNode = new TreeNode();
            treeNode.setId(sysOrg.getId());
            treeNode.setPid(sysOrg.getPid());
            treeNode.setKey(sysOrg.getId() + "");
            treeNode.setLabel(sysOrg.getName());
            treeNode.setType("org");
            treeNodes.add(treeNode);
        }
        List<DataEntry> dataEntries = dataEntryRepository.list();
        for(DataEntry dataEntry : dataEntries) {
            List<String> orgIds = StringUtils.splitList(dataEntry.getOrgId());
            for(String orgId : orgIds) {
                TreeNode<Long> treeNode = new TreeNode();
                treeNode.setId(dataEntry.getId());
                treeNode.setPid(Long.valueOf(orgId));
                treeNode.setKey(dataEntry.getId() + "");
                treeNode.setLabel(dataEntry.getName());
                treeNode.setType("layer");
                treeNodes.add(treeNode);
            }
        }
        return TreeUtils.buildTree(treeNodes);
    }

    @Override
    public void updateOne(DataEntry dataEntry) {
        dataEntryRepository.updateById(dataEntry);
    }

    @Transactional
    @Override
    public void removeById(Long dataEntryId) {
        dataEntryRepository.removeById(dataEntryId);
        dataEntrySetRepository.removeByEntryId(dataEntryId);
    }

    @Override
    public void create(DataEntry dataEntry) {
        dataEntryRepository.save(dataEntry);
    }

    @Override
    public void update(DataEntry dataEntry) {
        dataEntryRepository.updateById(dataEntry);
    }
}
