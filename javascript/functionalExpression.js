"use strict"

const abstractOp = f => (...exprs) => (...args) => f(...exprs.map(ex => ex(...args)));
const add = abstractOp((...vals) => vals.reduce((a, b) => a + b));
const subtract = abstractOp((a, b) => a - b);
const multiply = abstractOp((...vals) => vals.reduce((a, b) => a * b));
const divide = abstractOp((a, b) => a / b);
const negate = abstractOp(a => -a);
const cnst = c => abstractOp(a => c)();
const one = cnst(1)
const two = cnst(2)
const madd = abstractOp((a, b, c) => a * b + c)
const floor = abstractOp(a => Math.floor(a));
const ceil = abstractOp(a => Math.ceil(a));
const variable = name => (x, y, z) => {
    if (name === "x") {
        return x;
    }
    if (name === "y") {
        return y;
    }
    if (name === "z") {
        return z;
    }
};

let operations = [];
operations["+"] = [add, 2];
operations["-"] = [subtract, 2];
operations["/"] = [divide, 2];
operations["*"] = [multiply, 2];
operations["negate"] = [negate, 1];
operations["*+"] = [madd, 3];
operations["_"] = [floor, 1];
operations["^"] = [ceil, 1];
operations["one"] = [one, 0];
operations["two"] = [two, 0];
operations["x"] = [variable("x"), 0];
operations["y"] = [variable("y"), 0];
operations["z"] = [variable("z"), 0];


let parse = expression => {
    let operands = []
    for(const token of expression.split(' ').filter(s => s.length !== 0)) {
        if (isNaN(token)) {
            let op = operations[token]
            if (op[1] === 0) {
                operands.push(op[0]);
            } else {
                let expr = op[0](...operands.slice(-op[1]))
                operands = operands.slice(0, operands.length - op[1]);
                operands.push(expr);
            }
        } else {
            operands.push(cnst(parseInt(token)))
        }
    }
    return operands[0];
}
println(parse("x x 2 - * x * 1 +")(5, 0, 0))    