"use strict";
const React = require('react');
const FieldFeedbackPanel_1 = require('./FieldFeedbackPanel');
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = ({ object, error, name, label, options, onChange }) => {
    const handleOnChange = event => {
        console.log('select on change', event.target.value);
        onChange(name, event.target.value, null);
    };
    const selectedValue = object[name] || '';
    const fieldError = error && error.fieldErrors[name];
    const valid = !fieldError && selectedValue !== '';
    const cssGroup = `form-group ${fieldError ? 'has-error' : ''}`;
    return (React.createElement("div", {className: cssGroup}, 
        React.createElement("label", {className: 'col-sm-2 control-label'}, label), 
        React.createElement("div", {className: 'col-sm-10'}, 
            React.createElement("select", {size: 5, className: 'form-control', name: name, onChange: handleOnChange, value: selectedValue}, options.map(option => React.createElement("option", {key: option.value, value: option.value}, option.name))), 
            React.createElement(FieldFeedbackPanel_1.default, {valid: valid, fieldError: fieldError}))));
};
//# sourceMappingURL=SelectInput.js.map