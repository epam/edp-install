"use strict";
const React = require('react');
const util_1 = require('../../util');
const Input_1 = require('../form/Input');
const DateInput_1 = require('../form/DateInput');
const SelectInput_1 = require('../form/SelectInput');
;
class PetEditor extends React.Component {
    constructor(props) {
        super(props);
        this.onInputChange = this.onInputChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
        this.state = { editablePet: Object.assign({}, props.pet) };
    }
    onSubmit(event) {
        event.preventDefault();
        const { owner } = this.props;
        const { editablePet } = this.state;
        const request = {
            birthDate: editablePet.birthDate,
            name: editablePet.name,
            typeId: editablePet.typeId
        };
        const url = editablePet.isNew ? '/api/owners/' + owner.id + '/pets' : '/api/owners/' + owner.id + '/pets/' + editablePet.id;
        util_1.submitForm(editablePet.isNew ? 'POST' : 'PUT', url, request, (status, response) => {
            if (status === 204) {
                this.context.router.push({
                    pathname: '/owners/' + owner.id
                });
            }
            else {
                console.log('ERROR?!...', response);
                this.setState({ error: response });
            }
        });
    }
    onInputChange(name, value) {
        const { editablePet } = this.state;
        const modifiedPet = Object.assign({}, editablePet, { [name]: value });
        this.setState({ editablePet: modifiedPet });
    }
    render() {
        const { owner, pettypes } = this.props;
        const { editablePet, error } = this.state;
        const formLabel = editablePet.isNew ? 'Add Pet' : 'Update Pet';
        return (React.createElement("span", null, 
            React.createElement("h2", null, formLabel), 
            React.createElement("form", {className: 'form-horizontal', method: 'POST', action: util_1.url('/api/owner')}, 
                React.createElement("div", {className: 'form-group has-feedback'}, 
                    React.createElement("div", {className: 'form-group'}, 
                        React.createElement("label", {className: 'col-sm-2 control-label'}, "Owner"), 
                        React.createElement("div", {className: 'col-sm-10'}, 
                            owner.firstName, 
                            " ", 
                            owner.lastName)), 
                    React.createElement(Input_1.default, {object: editablePet, error: error, label: 'Name', name: 'name', onChange: this.onInputChange}), 
                    React.createElement(DateInput_1.default, {object: editablePet, error: error, label: 'Birth date', name: 'birthDate', onChange: this.onInputChange}), 
                    React.createElement(SelectInput_1.default, {object: editablePet, error: error, label: 'Type', name: 'typeId', options: pettypes, onChange: this.onInputChange})), 
                React.createElement("div", {className: 'form-group'}, 
                    React.createElement("div", {className: 'col-sm-offset-2 col-sm-10'}, 
                        React.createElement("button", {className: 'btn btn-default', type: 'submit', onClick: this.onSubmit}, formLabel)
                    )
                ))));
    }
}
PetEditor.contextTypes = {
    router: React.PropTypes.object.isRequired
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = PetEditor;
//# sourceMappingURL=PetEditor.js.map