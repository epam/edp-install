"use strict";
const React = require('react');
const react_router_1 = require('react-router');
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = ({ owner }) => (React.createElement("section", null, 
    React.createElement("h2", null, "Owner Information"), 
    React.createElement("table", {className: 'table table-striped'}, 
        React.createElement("tbody", null, 
            React.createElement("tr", null, 
                React.createElement("th", null, "Name"), 
                React.createElement("td", null, 
                    React.createElement("b", null, 
                        owner.firstName, 
                        " ", 
                        owner.lastName)
                )), 
            React.createElement("tr", null, 
                React.createElement("th", null, "Address"), 
                React.createElement("td", null, owner.address)), 
            React.createElement("tr", null, 
                React.createElement("th", null, "City"), 
                React.createElement("td", null, owner.city)), 
            React.createElement("tr", null, 
                React.createElement("th", null, "Telephone"), 
                React.createElement("td", null, owner.telephone)))
    ), 
    React.createElement(react_router_1.Link, {to: `/owners/${owner.id}/edit`, className: 'btn btn-default'}, "Edit Owner"), 
    " ", 
    React.createElement(react_router_1.Link, {to: `/owners/${owner.id}/pets/new`, className: 'btn btn-default'}, "Add New Pet")));
//# sourceMappingURL=OwnerInformation.js.map