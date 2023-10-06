#include "../include/Tree.hpp"

bool Scapegoat::contains(int value) const noexcept {
    return find(value);
}
bool Scapegoat::insert(int value) {
    if (m_size == 0) {
        m_root = new Node(value);
        m_size = 1;
        return true;
    }
    Node* u;
    Node* v = m_root;
    int d   = 0;
    while (true) {
        if (value < v->val) {
            if (v->left) {
                v = v->left;
            } else {
                u         = new Node(value);
                v->left   = u;
                u->parent = v;
                break;
            }
        } else {
            if (value > v->val) {
                if (v->right) {
                    v = v->right;
                } else {
                    u         = new Node(value);
                    v->right  = u;
                    u->parent = v;
                    break;
                }
            } else {
                return false;
            }
        }
        d++;
    };
    m_size++;
    if (d > log_alpha(m_size)) {
        Node* p = u->parent;
        while (size(p) <= m_alpha * size(p->parent)) {
            p = p->parent;
        }
        rebuild(p->parent);
    }
    return true;
}
bool Scapegoat::remove(int value) {
    Node* node = find(value);
    if (!node) {
        return false;
    }
    m_size--;
    if (node->right) {
        Node* min_node = node->right;
        while (min_node->left) {
            min_node = min_node->left;
        }
        node->val = min_node->val;
        Node* p   = min_node->parent;
        if (min_node->right) {
            if (p->left == min_node) {
                p->left = min_node->right;
            } else {
                p->right = min_node->right;
            }
            min_node->right->parent = p;
        } else {
            if (p->left == min_node) {
                p->left = nullptr;
            } else {
                p->right = nullptr;
            }
        }
        safe_delete(min_node);
    } else {
        Node* p = node->parent;
        if (p == nullptr) {
            m_root = node->left;
            if (m_root) {
                m_root->parent = nullptr;
            }
        } else {
            if (p->left == node) {
                p->left = node->left;
            } else {
                p->right = node->left;
            }
            if (node->left) {
                node->left->parent = p;
            }
        }
        safe_delete(node);
    }
    if (m_size > 0 && m_size < m_alpha * m_max_size) {
        rebuild(m_root);
    }
    return true;
}

std::size_t Scapegoat::size() const noexcept {
    return m_size;
}
bool Scapegoat::empty() const noexcept {
    return m_size == 0;
}
std::vector<int> Scapegoat::values() const noexcept {
    std::vector<int> vals;
    vals.reserve(m_size);
    inorder(m_root, vals);
    return vals;
}

Scapegoat::~Scapegoat() {
    if (m_size > 0) {
        Node** nodes = new Node*[m_size];
        inorder(m_root, nodes, 0);
        for (std::size_t i = 0; i < m_size; i++) {
            delete nodes[i];
        }
        delete[] nodes;
    }
}

void Scapegoat::set_alpha(double alpha) {
    if (alpha < 1 && 2 * alpha > 1) {
        m_alpha = alpha;
    }
}
// private:
Scapegoat::Node* Scapegoat::find(int value) const noexcept {
    Node* u = m_root;
    while (u && u->val != value) {
        u = (u->val < value ? u->right : u->left);
    }
    return u;
}
void Scapegoat::rebuild(Node* u) {
    int n    = size(u);
    Node* p  = u->parent;
    Node** a = new Node*[n];
    inorder(u, a, 0);
    if (p) {
        if (p->right == u) {
            p->right         = build(a, 0, n);
            p->right->parent = p;
        } else {
            p->left         = build(a, 0, n);
            p->left->parent = p;
        }
    } else {
        m_root         = build(a, 0, n);
        m_max_size     = m_size;
        m_root->parent = nullptr;
    }
    delete[] a;
}
Scapegoat::Node* Scapegoat::build(Node** a, int i, int n) {
    if (n == 0)
        return nullptr;
    int m     = n / 2;
    Node* mid = a[i + m];
    mid->left = build(a, i, m);
    if (mid->left) {
        mid->left->parent = mid;
    }
    mid->right = build(a, i + m + 1, n - m - 1);
    if (mid->right) {
        mid->right->parent = mid;
    }
    return mid;
}
void Scapegoat::safe_delete(Node* node) {
    node->left   = nullptr;
    node->right  = nullptr;
    node->parent = nullptr;
    delete node;
}
int Scapegoat::log_alpha(int n) {
    return static_cast<int>(std::log(n) / std::log(1.0 / m_alpha));
}
int Scapegoat::size(Node* node) {
    return (node ? 1 + size(node->left) + size(node->right) : 0);
}
int Scapegoat::inorder(Node* node, Node* nodes[], int i) {
    if (node) {
        i        = inorder(node->left, nodes, i);
        nodes[i] = node;
        return inorder(node->right, nodes, i + 1);
    }
    return i;
}
void Scapegoat::inorder(Node* node, std::vector<int>& v) const noexcept {
    if (node) {
        inorder(node->left, v);
        v.push_back(node->val);
        inorder(node->right, v);
    }
}