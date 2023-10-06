#ifndef TREE_HPP
#define TREE_HPP

#include <cmath>
#include <cstddef>
#include <cstdint>
#include <iostream>
#include <string>
#include <vector>

class Scapegoat {
private:
    struct Node {
        Node() = default;
        Node(int value, Node* left, Node* right, Node* parent) : left(left), right(right), parent(parent), val(value) {}
        explicit Node(int value) : left(nullptr), right(nullptr), parent(nullptr), val(value) {}
        Node* left{nullptr};
        Node* right{nullptr};
        Node* parent{nullptr};
        int val{0};
        ~Node() = default;
    };
    mutable Node* m_root{nullptr};
    std::size_t m_size{0};
    double m_alpha{0.81};
    std::size_t m_max_size{0};

public:
    Scapegoat() = default;
    [[nodiscard]] bool contains(int value) const noexcept;
    bool insert(int value);
    bool remove(int value);
    [[nodiscard]] std::size_t size() const noexcept;
    [[nodiscard]] bool empty() const noexcept;
    [[nodiscard]] std::vector<int> values() const noexcept;
    ~Scapegoat();

    void set_alpha(double alpha);

private:
    Node* find(int value) const noexcept;
    void rebuild(Node* u);
    Node* build(Node** a, int i, int n);
    void safe_delete(Node* node);
    int log_alpha(int n);
    int size(Node* node);
    int inorder(Node* node, Node* nodes[], int i);
    void inorder(Node* node, std::vector<int>& v) const noexcept;
};

#endif