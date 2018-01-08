"use strict";
const React = require('react');
const util_1 = require('../../util');
const Constraints_1 = require('../form/Constraints');
const DateInput_1 = require('../form/DateInput');
const Input_1 = require('../form/Input');
const PetDetails_1 = require('./PetDetails');
class VisitsPage extends React.Component {
    constructor(props) {
        super(props);
        this.onInputChange = this.onInputChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
    }
    componentDidMount() {
        const { params } = this.props;
        if (params && params.ownerId) {
            fetch(util_1.url(`/api/owner/${params.ownerId}`))
                .then(response => response.json())
                .then(owner => this.setState({
                owner: owner,
                visit: { id: null, isNew: true, date: null, description: '' }
            }));
        }
    }
    onSubmit(event) {
        event.preventDefault();
        const petId = this.props.params.petId;
        const { owner, visit } = this.state;
        const request = {
            date: visit.date,
            description: visit.description
        };
        const url = '/api/owners/' + owner.id + '/pets/' + petId + '/visits';
        util_1.submitForm('POST', url, request, (status, response) => {
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
        const { visit } = this.state;
        this.setState({ visit: Object.assign({}, visit, { [name]: value }) });
    }
    render() {
        if (!this.state) {
            return React.createElement("h2", null, "Loading...");
        }
        const { owner, error, visit } = this.state;
        const petId = this.props.params.petId;
        const pet = owner.pets.find(candidate => candidate.id.toString() === petId);
        return (React.createElement("div", null, 
            React.createElement("h2", null, "Visits"), 
            React.createElement("b", null, "Pet"), 
            React.createElement(PetDetails_1.default, {owner: owner, pet: pet}), 
            React.createElement("form", {className: 'form-horizontal', method: 'POST', action: util_1.url('/api/owner')}, 
                React.createElement("div", {className: 'form-group has-feedback'}, 
                    React.createElement(DateInput_1.default, {object: visit, error: error, label: 'Date', name: 'date', onChange: this.onInputChange}), 
                    React.createElement(Input_1.default, {object: visit, error: error, constraint: Constraints_1.NotEmpty, label: 'Description', name: 'description', onChange: this.onInputChange})), 
                React.createElement("div", {className: 'form-group'}, 
                    React.createElement("div", {className: 'col-sm-offset-2 col-sm-10'}, 
                        React.createElement("button", {className: 'btn btn-default', type: 'submit', onClick: this.onSubmit}, "Add Visit")
                    )
                ))));
    }
}
VisitsPage.contextTypes = {
    router: React.PropTypes.object.isRequired
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = VisitsPage;
//# sourceMappingURL=VisitsPage.js.map