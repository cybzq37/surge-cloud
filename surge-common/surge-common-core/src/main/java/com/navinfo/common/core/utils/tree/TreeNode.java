package com.surge.common.core.utils.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeNode<T> {
    private T id;
    private T pid;
    private String key;
    private String label;
    private String type;
    private List<TreeNode<T>> children = new ArrayList<>();

    public void addChild(TreeNode<T> child) {
        children.add(child);
    }
}
