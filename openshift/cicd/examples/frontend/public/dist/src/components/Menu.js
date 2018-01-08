"use strict";
const React = require('react');
const MenuItem = ({ active, url, title, children }) => (React.createElement("li", {className: active ? 'active' : ''}, 
    React.createElement("a", {href: url, title: title}, children)
));
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = ({ name }) => (React.createElement("nav", {className: 'navbar navbar-default', role: 'navigation'}, 
    React.createElement("div", {className: 'container'}, 
        React.createElement("div", {className: 'navbar-header'}, 
            React.createElement("a", {className: 'navbar-brand', href: '/'}, 
                React.createElement("span", null)
            ), 
            React.createElement("button", {type: 'button', className: 'navbar-toggle', "data-toggle": 'collapse', "data-target": '#main-navbar'}, 
                React.createElement("span", {className: 'icon-bar'}), 
                React.createElement("span", {className: 'icon-bar'}), 
                React.createElement("span", {className: 'icon-bar'}))), 
        React.createElement("div", {className: 'navbar-collapse collapse', id: 'main-navbar'}, 
            React.createElement("ul", {className: 'nav navbar-nav navbar-right'}, 
                React.createElement(MenuItem, {active: name === '/', url: '/', title: 'home page'}, 
                    React.createElement("span", {className: 'glyphicon glyphicon-home', "aria-hidden": 'true'}), 
                    " ", 
                    React.createElement("span", null, "Home")), 
                React.createElement(MenuItem, {active: name.startsWith('/owners'), url: '/owners/list', title: 'find owners'}, 
                    React.createElement("span", {className: 'glyphicon glyphicon-search', "aria-hidden": 'true'}), 
                    " ", 
                    React.createElement("span", null, "Find owners")), 
                React.createElement(MenuItem, {active: name === 'vets', url: '/vets', title: 'veterinarians'}, 
                    React.createElement("span", {className: 'glyphicon glyphicon-th-list', "aria-hidden": 'true'}), 
                    " ", 
                    React.createElement("span", null, "Veterinarians")), 
                React.createElement(MenuItem, {active: name === 'error', url: '/error', title: 'trigger a RuntimeException to see how it is handled'}, 
                    React.createElement("span", {className: 'glyphicon glyphicon-warning-sign', "aria-hidden": 'true'}), 
                    " ", 
                    React.createElement("span", null, "Error")))
        ))
));
//# sourceMappingURL=Menu.js.map