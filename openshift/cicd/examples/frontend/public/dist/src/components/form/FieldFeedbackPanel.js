"use strict";
const React = require('react');
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = ({ valid, fieldError }) => {
    if (valid) {
        return React.createElement("span", {className: 'glyphicon glyphicon-ok form-control-feedback', "aria-hidden": 'true'});
    }
    if (fieldError) {
        return (React.createElement("span", null, 
            React.createElement("span", {className: 'glyphicon glyphicon-remove form-control-feedback', "aria-hidden": 'true'}), 
            React.createElement("span", {className: 'help-inline'}, fieldError.message)));
    }
    return null;
};
//# sourceMappingURL=FieldFeedbackPanel.js.map