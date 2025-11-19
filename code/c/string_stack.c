// string_stack.c
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include "string_stack.h"

#define MIN_CAPACITY 16

struct _Stack {
    char **data;
    int size;
    int capacity;
};

// Internal helper to grow/shrink the underlying array.
// Returns true on success, false on allocation failure (leaves stack unchanged).
static bool resize_stack(stack s, int new_capacity) {
    if (new_capacity < MIN_CAPACITY) {
        new_capacity = MIN_CAPACITY;
    }
    if (new_capacity > MAX_CAPACITY) {
        new_capacity = MAX_CAPACITY;
    }
    if (new_capacity == s->capacity) {
        return true;
    }

    char **new_data = realloc(s->data, sizeof(char *) * new_capacity);
    if (new_data == NULL) {
        return false;
    }

    s->data = new_data;
    s->capacity = new_capacity;
    return true;
}

stack_response create() {
    stack_response res;
    res.stack = NULL;
    res.code = success;

    stack s = malloc(sizeof(*s));
    if (s == NULL) {
        res.code = out_of_memory;
        return res;
    }

    s->capacity = MIN_CAPACITY;
    s->size = 0;
    s->data = malloc(sizeof(char *) * s->capacity);
    if (s->data == NULL) {
        free(s);
        res.code = out_of_memory;
        return res;
    }

    res.stack = s;
    res.code = success;
    return res;
}

int size(const stack s) {
    if (s == NULL) return 0;
    return s->size;
}

bool is_empty(const stack s) {
    if (s == NULL) return true;
    return s->size == 0;
}

bool is_full(const stack s) {
    if (s == NULL) return false;
    return s->size == MAX_CAPACITY;
}

response_code push(stack s, char* item) {
    if (s == NULL) return out_of_memory;

    if (item == NULL) {
        // Spec doesn't define behavior for NULL strings; treat as too large / invalid.
        return stack_element_too_large;
    }

    size_t len = strlen(item);
    // We allow at most MAX_ELEMENT_BYTE_SIZE-1 chars + '\0'
    if (len >= MAX_ELEMENT_BYTE_SIZE) {
        return stack_element_too_large;
    }

    if (s->size == MAX_CAPACITY) {
        return stack_full;
    }

    if (s->size == s->capacity) {
        int new_capacity = s->capacity * 2;
        if (new_capacity > MAX_CAPACITY) {
            new_capacity = MAX_CAPACITY;
        }
        if (!resize_stack(s, new_capacity)) {
            return out_of_memory;
        }
    }

    char *copy = malloc(len + 1);
    if (copy == NULL) {
        return out_of_memory;
    }
    memcpy(copy, item, len + 1);

    s->data[s->size] = copy;
    s->size += 1;

    return success;
}

string_response pop(stack s) {
    string_response res;
    res.code = success;
    res.string = NULL;

    if (s == NULL || s->size == 0) {
        res.code = stack_empty;
        res.string = NULL;
        return res;
    }

    // Element to pop is at index size-1
    char *stored = s->data[s->size - 1];
    size_t len = strlen(stored);

    char *copy = malloc(len + 1);
    if (copy == NULL) {
        // Do not modify the stack if we cannot allocate the return string
        res.code = out_of_memory;
        res.string = NULL;
        return res;
    }

    memcpy(copy, stored, len + 1);
    free(stored);
    s->data[s->size - 1] = NULL;
    s->size -= 1;

    // Shrink if size is less than 1/4 of capacity, but never below MIN_CAPACITY
    if (s->capacity > MIN_CAPACITY && s->size < s->capacity / 4) {
        int new_capacity = s->capacity / 2;
        if (new_capacity < MIN_CAPACITY) {
            new_capacity = MIN_CAPACITY;
        }
        // If shrinking fails, we just keep the existing capacity
        resize_stack(s, new_capacity);
    }

    res.code = success;
    res.string = copy;
    return res;
}

void destroy(stack* s_ptr) {
    if (s_ptr == NULL || *s_ptr == NULL) {
        return;
    }

    stack s = *s_ptr;

    for (int i = 0; i < s->size; i++) {
        free(s->data[i]);
    }

    free(s->data);
    free(s);

    *s_ptr = NULL;
}
