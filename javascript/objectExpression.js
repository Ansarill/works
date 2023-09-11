
"use strict"   

function expressionFactory(toString, prefix, postfix, evaluate, diff) {
    function Expr(...data) {
        this._data = data
    }
    Expr.prototype.toString = toString
    Expr.prototype.prefix = prefix,
    Expr.prototype.postfix = postfix,
    Expr.prototype.evaluate = evaluate
    Expr.prototype.diff = diff
    Expr.prototype.empty = function () {
        return this._data.length === 0
    };
    Expr.prototype.constructor = Expr
    return Expr
}

const firstVar = function() {
    return this._data[0].toString()
}

const Const = expressionFactory(
    firstVar,
    firstVar,
    firstVar,
    function() {
        return parseFloat(this._data[0])
    },
    function(name) {
        return Const.ZERO;
    }
) 
Const.TWO = new Const(2)
Const.ZERO = new Const(0)
Const.ONE = new Const(1)

const Variable = expressionFactory(
    firstVar,
    firstVar,
    firstVar,
    function(...args) {
        return args[Variable._VARIABLES.get(this._data[0])]
    },
    function(name) {
        return this._data[0] === name? Const.ONE: Const.ZERO;
    }
)
Variable._VARIABLES = new Map()
Variable._VARIABLES.set('x', 0)
Variable._VARIABLES.set('y', 1)
Variable._VARIABLES.set('z', 2)
Variable.hasVariable = name => Variable._VARIABLES.has(name);


const AbstractOp = expressionFactory(
    function() {
        return (!this.empty()? this._data.map(ex => ex.toString()).join(' ') : '') + ' ' + this._opSym();
    },
    function () {
        return '(' + this._opSym() + ' ' + (!this.empty()? this._data.map(ex => ex.prefix()).join(' '): '') + ')'
    },
    function() {
        return '(' + (!this.empty()? this._data.map(ex => ex.postfix()).join(' '): '') + ' ' + this._opSym() + ')'
    },
    function(...args) {
        return this._func(...this._data.map(ex => ex.evaluate(...args)))
    },
    () => undefined
)

function operationFactory(opSym, func, diff) {
    function Operation(...exprs) {
        AbstractOp.call(this, ...exprs)
    }
    Operation.prototype = Object.create(AbstractOp.prototype)
    Operation.prototype.constructor = Operation
    Operation.prototype._opSym = opSym
    Operation.toString = opSym
    Operation.prototype._func = func
    Operation.prototype.diff = diff
    return Operation
}

const Add = operationFactory(
    () => '+', 
    (...vals) => vals.reduce((a, b) => a + b),
    function(name) { return new Add(...this._data.map(ex => ex.diff(name))) }
)

const Multiply = operationFactory(
    () => '*',
    (...vals) => vals.reduce((a, b) => a * b),
    function(name) { 
        const a = this._data[0]
        const b = this._data[1]
        return new Add(new Multiply(a, b.diff(name)), new Multiply(b, a.diff(name)))
    }
)

const Subtract = operationFactory(
    () => '-',
    (a, b) => a - b,
    function (name) { return new Subtract(...this._data.map(ex => ex.diff(name))) }
)

const Divide = operationFactory(
    () => '/',
    (a, b) => a / b,
    function(name) {
        let a = this._data[0]
        let b = this._data[1]
        return new Divide (
            new Subtract (new Multiply(a.diff(name), b), 
                            new Multiply(b.diff(name), a)),
            new Multiply(b, b)
        )
    }
)

const Negate = operationFactory(
    () => 'negate',
    a => -a,
    function(name) {
        return new Negate(this._data[0].diff(name));
    }
)
const funcSumSqN = (...vals) => vals.map(x => x * x).reduce((a, b) => a + b)
const SumSqN = operationFactory(
    function() {
        return 'sumsq' + (this._data !== undefined? this._data.length : "")
    },
    funcSumSqN,
    function (name) {
        return new Add(...this._data.map(ex => new Multiply(ex, ex))).diff(name)
    }
)

const Sumsq2 = SumSqN, Sumsq3 = SumSqN, Sumsq4 = SumSqN, Sumsq5 = SumSqN

const DistanceN = operationFactory(
    function() {
        return 'distance' + (this._data !== undefined? this._data.length : "")
    },
    (...vals) => Math.sqrt(funcSumSqN(...vals)), 
    function (name) {
        return new Divide(new SumSqN(...this._data).diff(name), new Multiply(Const.TWO, new DistanceN(...this._data)))
    }
)
const Distance2 = DistanceN, Distance3 = DistanceN, Distance4 = DistanceN, Distance5 = DistanceN

function funcSumexp(...vals) {
    if (vals.length === 0) {
        return 0
    } else {
        return vals.map(x => Math.pow(Math.E, x)).reduce((a, b) => a + b)
    }
}

const Sumexp = operationFactory(
    function() {
        return "sumexp"
    },
    funcSumexp,
    function (name) {
        if (this.empty()) {
            return Const.ZERO
        } else {
            return new Add(...this._data.map(ex => new Multiply(ex.diff(name), new Sumexp(ex))))
        }
    }
)

const LSE = operationFactory(
    function() {
        return "lse"
    },
    (...vals) => Math.log(funcSumexp(...vals)),
    function(name) {
        const sumexp = new Sumexp(...this._data)
        return new Divide(sumexp.diff(name), sumexp)
    }
)

let ex = new Subtract(
    new Multiply(
        new Const(2),
        new Variable("x")
    ),
    new Const(3)
);
ex = new Sumexp(new Variable('x'))
println("'" + ex.toString() + "'")
println(ex.evaluate(5, 0, 0))
ex = new Const(10.0000000)
println(ex.evaluate())
println(typeof ex.toString())
println(typeof ex.evaluate())
ex =  new Multiply(new Const(4), new Variable('z'))
println(ex.diff('z'))
ex = new Divide(new Const(5), new Variable('z')).diff('z')
println(ex.evaluate(2, 2, 2))
println(ex.diff('z'))

const OPERATIONS = new Map()
OPERATIONS.set("+", [Add, 2]);
OPERATIONS.set("-", [Subtract, 2]);
OPERATIONS.set("/", [Divide, 2]);
OPERATIONS.set("*", [Multiply, 2]);
OPERATIONS.set("negate", [Negate, 1]);
OPERATIONS.set('sumsq', SumSqN);
OPERATIONS.set('distance', DistanceN);
OPERATIONS.set('lse', [LSE, Infinity])
OPERATIONS.set('sumexp', [Sumexp, Infinity])
OPERATIONS.getOperationAndArity = function (token) {
    if (typeof(token) !== 'string') {
        return undefined;
    }
    let pr = OPERATIONS.get(token)
    if (pr !== undefined) {
        return pr
    } else {
        pr = OPERATIONS.get(token.slice(0, -1));
        if (pr !== undefined) {
            return [pr, parseInt(token[token.length - 1])]
        } else {
            return undefined;
        }
    }
}
println(Variable.hasVariable('x'))
let parse = expression => {
    let operands = []
    for (const token of expression.split(' ').filter(s => s.length !== 0)) {
        if (Variable.hasVariable(token)) {
            operands.push(new Variable(token))
        } else {
            if (isNaN(token)) {
                let op = OPERATIONS.getOperationAndArity(token);
                operands.push(new op[0](...operands.splice(-op[1])))        
            } else {
                operands.push(new Const(token))
            }
        }
    }
    return operands.pop();
}

function ErrorFactory (toSuper, name, message) {
    function Error(...data) {
        this.message = message(...data)
    }
    Error.prototype = Object.create(toSuper.prototype)
    Error.prototype.constructor = Error
    Error.prototype.name = name
    return Error
} 

const ParsingError = ErrorFactory(Error, "ParsingError", s => s.toString())
function partError(error) {
    return (expression, start, end, ...data) => expression.slice(0, start) + "--->'" + expression.slice(start, end) + "'<--- " + error(expression, start, end, ...data)
}
function unexpectedError(error) {
    return (expression, pos, ...data) => expression.slice(0, pos) + "--->'" + (data.length > 0? data[0]: expression[pos]) + "'<--- " + error(...data)
}
const MissingOperationError = ErrorFactory(
    ParsingError, "MissingOperationError", 
    unexpectedError((...data) => "expected an operation, but found: '" + data[0] + "'")
)
const UnknownOperationError = ErrorFactory(
    ParsingError, "UnknownOperationError", 
    partError(() => "unknown operation")
)
const UnknownVariableNameError = ErrorFactory(
    ParsingError, "UnknownVariableNameError", 
    partError(() => "invalid variable name")
)
const MismatchBracketError = ErrorFactory(
    ParsingError, "MismatchBracketError", 
    partError((ex, st, en) => (ex[st] === '('? "missing": "unmatched") + " closing bracket ')'")
)
const MissingArgumentError = ErrorFactory(
    ParsingError, "MissingArgumentError",
    partError((ex, st, en, ...data) => "argument is missing for the operation '" + data[0] + "' with arity = " + data[1])
)
const MissingPrimaryExpressionError = ErrorFactory(
    ParsingError, "MissingPrimaryExpressionError",
    unexpectedError((...data) => "expected a primary expression")
)
const UnknowMode = ErrorFactory(
    Error, "UnknownMode",
    (mode) => "Unknown mode: " + mode 
)
const InvalidNumberError = ErrorFactory(
    ParsingError, "InvalidNumberError",
    partError(() => "isNan")
)

class BaseParser {
    #EOF
    constructor(expression) {
        this.expression = expression
        this.pos = 0
        this.#EOF = '\0'
    }
    eof() {
        return this.pos >= this.expression.length
    }
    cur() {
        return !this.eof()? this.expression[this.pos]: this.#EOF;
    }
    tokenEnd() {
        return this.eof() || this.test(' ') || this.test(')') || this.test('(')
    }
    test(f) {
        if (typeof(f) === 'function') {
            return f(this.cur())
        } else {
            return this.cur() === f
        }
    }
    skipWhitespaces () {
        while(!this.eof() && this.test(' ')) {
            this.pos++
        }
    }
    takeTokenAndExpect(condition) {
        const k = this.pos
        while(!this.tokenEnd() && this.test(condition)) {
            this.pos += 1
        }
        if (this.tokenEnd()) {
            return this.expression.slice(k, this.pos)
        } else {
            this.pos = k
            return undefined
        }
    }
}

class Parser extends BaseParser {
    constructor(expression, mode) {
        super(expression)
        this.er = 0
        switch (mode) {
            case "prefix":
                this.mode = true
                break
            case "postfix":
                this.mode = false
                break
            default:
                throw new UnknowMode(mode)
        }
    }
    static between(c, a, b) {
        return a <= c && c <= b
    }
    static isLetter (c) {
        return Parser.between(c, 'a', 'z') || Parser.between(c, 'A', 'Z');
    }
    static isDigit(c) {
        return Parser.between(c, '0', '9')
    }
    static canBeNumberPart(c) {
        return Parser.isDigit(c) || (c === '-') || (c === '.')
    }
    static canBeVariableNamePart(c) {
        return Parser.isLetter(c)
    }
    static canBeOperationNamePart(c) {
        return Parser.isLetter(c) || Parser.isDigit(c) || c === '*' || c === '+' || c === '-' || c === '/'
    }
    parse() {
        const ex = this.parseExpression(true)
        if (ex === undefined) {
            throw new ParsingError("expression is empty")
        }
        this.skipWhitespaces()
        this.er = this.pos
        if (this.eof()) {
            return ex;
        } else {
            throw new ParsingError("Excessive info--->'" + this.expression.slice(this.er))
        }
    }
    parseExpression(expect) {
        this.er = this.pos
        this.skipWhitespaces()
        if (this.eof() || this.test(')')) {
            return undefined
        }
        if (this.test('(')) {
            return this.parseInside()
        } else {
            return this.parsePrimary(expect)
        }
    }
    parseInside() {
        let op = [undefined, Infinity];
        let args = []
        const leftBracket = this.pos
        this.pos += 1
        if (this.mode) {
            op = this.parseOperation()
        }
        for (let k = 0; k < op[1]; k++) {
            const ex = this.parseExpression(this.mode)
            if (ex !== undefined) {
                args.push(ex)
            } else {
                break
            }
        }
        if (!this.mode) {
            op = this.parseOperation()
        }
        if ((args.length != op[1] || args.length == 0) && op[1] != Infinity) {
            throw new MissingArgumentError(this.expression, this.er, this.pos, op[0].toString(), op[1]);
        }
        this.skipWhitespaces()
        if (this.cur() === ')') {
            this.pos += 1
            return new op[0](...args)
        } else {
            throw new MismatchBracketError(this.expression, leftBracket, this.pos)
        }
    }
    parseOperation() {
        this.skipWhitespaces()
        this.er = this.pos
        const operation = OPERATIONS.getOperationAndArity(this.takeTokenAndExpect(Parser.canBeOperationNamePart))
        if (operation !== undefined) {
            return operation
        } else {
            if (this.er === this.pos) {
                throw new MissingOperationError(this.expression, this.er, this.cur());
            } else {
                throw new UnknownOperationError(this.expression, this.er, this.pos)
            }
        }
    }
    parsePrimary(expect) {
        this.er = this.pos
        let ex = this.takeTokenAndExpect(Parser.canBeNumberPart)
        if (ex !== undefined) {
            if (isNaN(ex)) {
                if (expect) {
                    throw new InvalidNumberError(this.expression, this.er, this.pos)
                }
            } else {
                return new Const(ex)
            }
        }
        ex = this.takeTokenAndExpect(Parser.canBeVariableNamePart)
        if (ex !== undefined) {
            if (Variable._VARIABLES.has(ex)) {
                return new Variable(ex)
            } else if (expect) {
                throw new UnknownVariableNameError(this.expression, this.er, this.pos)
            }
        } 
        if (expect) {
            if (this.test(')')) {
                throw new MismatchBracketError(this.expression, this.er - 1, this.er)
            } else {
                throw new MissingPrimaryExpressionError(this.expression, this.pos)
            }
        }
        this.pos = this.er
        return undefined;
    }
}

function parsePrefix(expression) {
    return new Parser(expression, "prefix").parse()
}
function parsePostfix(expression) {
    return new Parser(expression, "postfix").parse()
}