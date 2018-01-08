"use strict";
const React = require('react');
const FieldFeedbackPanel_1 = require('./FieldFeedbackPanel');
const NoConstraint = {
    message: '',
    validate: v => true
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = ({ object, error, name, constraint = NoConstraint, label, onChange }) => {
    const handleOnChange = event => {
        const { value } = event.target;
        let error = null;
        const fieldError = constraint.validate(value) === false ? { field: name, message: constraint.message } : null;
        onChange(name, value, fieldError);
    };
    const value = object[name];
    const fieldError = error && error.fieldErrors[name];
    const valid = !fieldError && value !== null && value !== undefined;
    const cssGroup = `form-group ${fieldError ? 'has-error' : ''}`;
    return (React.createElement("div", {className: cssGroup}, 
        React.createElement("label", {className: 'col-sm-2 control-label'}, label), 
        React.createElement("div", {className: 'col-sm-10'}, 
            React.createElement("input", {type: 'text', name: name, className: 'form-control', value: value, onChange: handleOnChange}), 
            React.createElement(FieldFeedbackPanel_1.default, {valid: valid, fieldError: fieldError}))));
};
//# sourceMappingURL=Input.js.map