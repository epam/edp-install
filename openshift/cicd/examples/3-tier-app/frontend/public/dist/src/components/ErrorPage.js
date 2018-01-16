"use strict";
const React = require('react');
class ErrorPage extends React.Component {
    constructor() {
        super();
        this.state = {};
    }
    componentDidMount() {
        fetch('/api/oups')
            .then(response => response.json())
            .then(error => this.setState({ error }));
    }
    render() {
        const { error } = this.state;
        return React.createElement("span", null, 
            React.createElement("img", {src: '/images/pets.png'}), 
            React.createElement("h2", null, "Something happened..."), 
            error ?
                React.createElement("span", null, 
                    React.createElement("p", null, 
                        React.createElement("b", null, "Status:"), 
                        " ", 
                        error.status), 
                    React.createElement("p", null, 
                        React.createElement("b", null, "Message:"), 
                        " ", 
                        error.message))
                :
                    React.createElement("p", null, 
                        React.createElement("b", null, "Unkown error")
                    ));
    }
}
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = ErrorPage;
;
//# sourceMappingURL=ErrorPage.js.map