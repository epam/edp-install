"use strict";
const React = require('react');
const util_1 = require('../../util');
const Input_1 = require('../form/Input');
const Constraints_1 = require('../form/Constraints');
;
class OwnerEditor extends React.Component {
    constructor(props) {
        super(props);
        this.onInputChange = this.onInputChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
        this.state = {
            owner: Object.assign({}, props.initialOwner)
        };
    }
    onSubmit(event) {
        event.preventDefault();
        const { owner } = this.state;
        const url = owner.isNew ? '/api/owner' : '/api/owner/' + owner.id;
        util_1.submitForm(owner.isNew ? 'POST' : 'PUT', url, owner, (status, response) => {
            if (status === 200 || status === 201) {
                const newOwner = response;
                this.context.router.push({
                    pathname: '/owners/' + newOwner.id
                });
            }
            else {
                console.log('ERROR?!...', response);
                this.setState({ error: response });
            }
        });
    }
    onInputChange(name, value, fieldError) {
        const { owner, error } = this.state;
        const modifiedOwner = Object.assign({}, owner, { [name]: value });
        const newFieldErrors = error ? Object.assign({}, error.fieldErrors, { [name]: fieldError }) : { [name]: fieldError };
        this.setState({
            owner: modifiedOwner,
            error: { fieldErrors: newFieldErrors }
        });
    }
    render() {
        const { owner, error } = this.state;
        return (React.createElement("span", null, 
            React.createElement("h2", null, "New Owner"), 
            React.createElement("form", {className: 'form-horizontal', method: 'POST', action: util_1.url('/api/owner')}, 
                React.createElement("div", {className: 'form-group has-feedback'}, 
                    React.createElement(Input_1.default, {object: owner, error: error, constraint: Constraints_1.NotEmpty, label: 'First Name', name: 'firstName', onChange: this.onInputChange}), 
                    React.createElement(Input_1.default, {object: owner, error: error, constraint: Constraints_1.NotEmpty, label: 'Last Name', name: 'lastName', onChange: this.onInputChange}), 
                    React.createElement(Input_1.default, {object: owner, error: error, constraint: Constraints_1.NotEmpty, label: 'Address', name: 'address', onChange: this.onInputChange}), 
                    React.createElement(Input_1.default, {object: owner, error: error, constraint: Constraints_1.NotEmpty, label: 'City', name: 'city', onChange: this.onInputChange}), 
                    React.createElement(Input_1.default, {object: owner, error: error, constraint: Constraints_1.Digits(10), label: 'Telephone', name: 'telephone', onChange: this.onInputChange})), 
                React.createElement("div", {className: 'form-group'}, 
                    React.createElement("div", {className: 'col-sm-offset-2 col-sm-10'}, 
                        React.createElement("button", {className: 'btn btn-default', type: 'submit', onClick: this.onSubmit}, owner.isNew ? 'Add Owner' : 'Update Owner')
                    )
                ))));
    }
}
OwnerEditor.contextTypes = {
    router: React.PropTypes.object.isRequired
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = OwnerEditor;
//# sourceMappingURL=OwnerEditor.js.map