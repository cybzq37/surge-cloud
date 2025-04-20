package com.surge.common.core.utils.tree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TreeUtils {

    public static <T> List<TreeNode> buildTree(List<TreeNode<T>> nodes) {
        Map<T, TreeNode<T>> map = new HashMap<>();
        for (TreeNode current : nodes) {
            map.put((T) current.getId(), current);
        }

        for (TreeNode current : nodes) {
            T parentId = (T) current.getPid();
            if(parentId == null || String.valueOf(parentId).equals("0")) {
                continue;
            }
            TreeNode parent = map.get(parentId);
            if (parent != null) {
                parent.addChild(current);
            }
        }
        return map.values().stream()
                .filter(node -> node.getPid() == null
                        || String.valueOf(node.getPid()).equals("0")
                ).collect(Collectors.toList());
    }
}
