export class Stack {
    constructor() {
        this.items = [];  // 스택을 저장할 배열
    }

    // 스택에 요소를 추가 (Push)
    push(element) {
        this.items.push(element);
    }

    // 스택에서 요소를 제거하고 반환 (Pop)
    pop() {
        if (this.isEmpty()) {
            console.log("스택이 비어있습니다.");
            return null;
        }
        return this.items.pop();
    }

    // 스택의 맨 위 요소를 확인 (Peek)
    peek() {
        if (this.isEmpty()) {
            console.log("스택이 비어있습니다.");
            return null;
        }
        return this.items[this.items.length - 1];
    }

    // 스택이 비어있는지 확인 (isEmpty)
    isEmpty() {
        return this.items.length === 0;
    }

    // 스택의 크기 반환 (Size)
    size() {
        return this.items.length;
    }

}

// 예제 사용법
// const stack = new Stack();
//
// stack.push(10);
// stack.push(20);
// stack.push(30);
//
// console.log("스택의 맨 위 요소:", stack.peek());  // 30
//
//
// console.log("Pop된 요소:", stack.pop());  // 30
//
// console.log("스택이 비어있는가?", stack.isEmpty());  // false
//
// console.log("스택 크기:", stack.size());  // 2
