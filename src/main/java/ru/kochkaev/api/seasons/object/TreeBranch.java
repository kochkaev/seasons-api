package ru.kochkaev.api.seasons.object;

import java.util.*;
import java.util.stream.Collectors;

public class TreeBranch<T> {
    protected T component;
    protected final TreeBranch<T> parent;
    protected final Set<TreeBranch<T>> branches = new HashSet<>();

    public TreeBranch(T component, TreeBranch<T> parent) {
        this.component = component;
        this.parent = parent;
    }

    public T get() {
        return component;
    }
    public void set(T component) {
        this.component = component;
    }

    public void add(T component) {
        addBranch(new TreeBranch<>(component, this));
    }
    public void remove(T component) {
        removeBranches(findAllInBranch(component));
    }
    public void removeAll(Collection<T> components) {
        components.stream().map(this::findAllInBranch).forEach(this::removeBranches);
    }
    public TreeBranch<T> find(T component) {
        if (component.equals(this.component)) return this;
        else return branches.stream().map(branch -> branch.find(component)).filter(Objects::nonNull).findAny().orElse(null);
    }
    public Collection<TreeBranch<T>> findAll(T component) {
        Set<TreeBranch<T>> output = new HashSet<>();
        if (component.equals(this.component)) output.add(this);
        branches.stream().map(branch -> branch.findAll(component)).forEach(output::addAll);
        return output;
    }
    public TreeBranch<T> findInBranch(T component) {
        return branches.stream().filter(branch -> branch.is(component)).findAny().orElse(null);
    }
    public Collection<TreeBranch<T>> findAllInBranch(T component) {
        return branches.stream().filter(branch -> branch.is(component)).collect(Collectors.toSet());
    }
    public boolean contains(T component) {
        if (component.equals(this.component)) return true;
        else return branches.stream().map(branch -> branch.contains(component)).findAny().orElse(false);
    }
    public boolean haveBranchOf(T component) {
        return branches.stream().map(TreeBranch::get).anyMatch(branch -> branch.equals(component));
    }
    public boolean hasComponent() {
        return component!= null;
    }
    public boolean isBranchOf(T component) {
        if (component.equals(this.component)) return true;
        else if (parent != null) return parent.isBranchOf(component);
        else return false;
    }
    public boolean is(T component) {
        return component.equals(this.component);
    }

    public Collection<TreeBranch<T>> getBranches() {
        return branches;
    }
    public TreeBranch<T> getBranch(T component) {
        return branches.stream().filter(branch -> branch.get().equals(component)).findFirst().orElse(null);
    }
    public TreeBranch<T> getAnyBranch() {
        return branches.stream().findAny().orElse(null);
    }
    public final void addBranch(TreeBranch<T> branch) {
        this.branches.add(branch);
    }
    public void addBranches(Collection<TreeBranch<T>> branches) {
        this.branches.addAll(branches);
    }
    public final void removeBranch(TreeBranch<T> branch) {
        this.branches.remove(branch);
    }
    public void removeBranches(Collection<TreeBranch<T>> branches) {
        this.branches.removeAll(branches);
    }

    public TreeBranch<T> getParent() {
        return parent;
    }

    public Collection<TreeBranch<T>> getBranchesSet() {
        Set<TreeBranch<T>> output = new HashSet<>();
        branches.forEach(branch -> {
            output.add(branch);
            output.addAll(branch.getBranchesSet());
        });
        return output;
    }
    public Collection<T> getBranchesComponentsSet() {
        Set<T> output = new HashSet<>();
        branches.forEach(branch -> {
            output.add(branch.get());
            output.addAll(branch.getBranchesComponentsSet());
        });
        return output;
    }

    public Deque<TreeBranch<T>> getBranchPathDeque() {
        Deque<TreeBranch<T>> output = new ArrayDeque<>();
        TreeBranch<T> branch = this;
        while (branch != null) {
            output.addFirst(branch);
            branch = branch.parent;
        }
        return output;
    }
    public TreeBranch<T> getBranchByComponentsPathDeque(Deque<T> path) {
        TreeBranch<T> branch = this;
        for (T component : path) {
            if (branch.haveBranchOf(component)) branch = branch.getBranch(component);
            else return null;
        }
        return branch;
    }

    public void clearBranches() {
        branches.clear();
    }
    public void clear() {
        branches.clear();
        component = null;
    }
    public void delete() {
        parent.removeBranch(this);
    }
}
