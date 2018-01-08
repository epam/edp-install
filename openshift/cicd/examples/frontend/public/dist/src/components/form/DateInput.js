"use strict";
const React = require('react');
const ReactDatePicker = require('react-datepicker');
const moment = require('moment');
const FieldFeedbackPanel_1 = require('./FieldFeedbackPanel');
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = ({ object, error, name, label, onChange }) => {
    const handleOnChange = value => {
        const dateString = value ? value.format('YYYY/MM/DD') : null;
        onChange(name, dateString, null);
    };
    const selectedValue = object[name] ? moment(object[name], 'YYYY/MM/DD') : null;
    const fieldError = error && error.fieldErrors[name];
    const valid = !fieldError && selectedValue != null;
    const cssGroup = `form-group ${fieldError ? 'has-error' : ''}`;
    return (React.createElement("div", {className: cssGroup}, 
        React.createElement("label", {className: 'col-sm-2 control-label'}, label), 
        React.createElement("div", {className: 'col-sm-10'}, 
            React.createElement(ReactDatePicker, {selected: selectedValue, onChange: handleOnChange, className: 'form-control', dateFormat: 'YYYY-MM-DD'}), 
            React.createElement("span", {className: 'glyphicon glyphicon-ok form-control-feedback', "aria-hidden": 'true'}), 
            React.createElement(FieldFeedbackPanel_1.default, {valid: valid, fieldError: fieldError}))));
};
//# sourceMappingURL=DateInput.js.map