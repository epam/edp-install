"use strict";
const React = require('react');
const ReactDOM = require('react-dom');
const react_hot_loader_1 = require('react-hot-loader');
const react_router_1 = require('react-router');
require('./styles/less/petclinic.less');
const Root_1 = require('./Root');
const mountPoint = document.getElementById('mount');
ReactDOM.render(React.createElement(react_hot_loader_1.AppContainer, null, 
    React.createElement(Root_1.default, {history: react_router_1.browserHistory})
), mountPoint);
if (module.hot) {
    module.hot.accept('./Root', () => {
        const NextApp = require('./Root').default;
        ReactDOM.render(React.createElement(react_hot_loader_1.AppContainer, null, 
            React.createElement(NextApp, {history: react_router_1.browserHistory})
        ), mountPoint);
    });
}
//# sourceMappingURL=main.js.map