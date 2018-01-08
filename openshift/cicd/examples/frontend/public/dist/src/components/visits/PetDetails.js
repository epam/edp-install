"use strict";
const React = require('react');
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = ({ owner, pet }) => (React.createElement("table", {className: 'table table-striped'}, 
    React.createElement("thead", null, 
        React.createElement("tr", null, 
            React.createElement("th", null, "Name"), 
            React.createElement("th", null, "Birth Date"), 
            React.createElement("th", null, "Type"), 
            React.createElement("th", null, "Owner"))
    ), 
    React.createElement("tbody", null, 
        React.createElement("tr", null, 
            React.createElement("td", null, pet.name), 
            React.createElement("td", null, pet.birthDate), 
            React.createElement("td", null, pet.type.name), 
            React.createElement("td", null, 
                owner.firstName, 
                " ", 
                owner.lastName))
    )));
//# sourceMappingURL=PetDetails.js.map