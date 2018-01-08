"use strict";
const React = require('react');
const react_router_1 = require('react-router');
const util_1 = require('../../util');
const OwnersTable_1 = require('./OwnersTable');
const getFilterFromLocation = (location) => {
    return location.query ? location.query.lastName : null;
};
class FindOwnersPage extends React.Component {
    constructor(props) {
        super(props);
        this.onFilterChange = this.onFilterChange.bind(this);
        this.submitSearchForm = this.submitSearchForm.bind(this);
        this.state = {
            filter: getFilterFromLocation(props.location)
        };
    }
    componentDidMount() {
        const { filter } = this.state;
        if (typeof filter === 'string') {
            this.fetchData(filter);
        }
    }
    componentWillReceiveProps(nextProps) {
        const { location } = nextProps;
        const filter = getFilterFromLocation(location);
        this.setState({ filter });
        this.fetchData(filter);
    }
    onFilterChange(event) {
        this.setState({
            filter: event.target.value
        });
    }
    submitSearchForm() {
        const { filter } = this.state;
        this.context.router.push({
            pathname: '/owners/list',
            query: { 'lastName': filter || '' }
        });
    }
    fetchData(filter) {
        const query = filter ? encodeURIComponent(filter) : '';
        const requestUrl = util_1.url('api/owner/list?lastName=' + query);
        fetch(requestUrl)
            .then(response => response.json())
            .then(owners => { this.setState({ owners }); });
    }
    render() {
        const { filter, owners } = this.state;
        return (React.createElement("span", null, 
            React.createElement("section", null, 
                React.createElement("h2", null, "Find Owners"), 
                React.createElement("form", {className: 'form-horizontal', action: 'javascript:void(0)'}, 
                    React.createElement("div", {className: 'form-group'}, 
                        React.createElement("div", {className: 'control-group', id: 'lastName'}, 
                            React.createElement("label", {className: 'col-sm-2 control-label'}, "Last name "), 
                            React.createElement("div", {className: 'col-sm-10'}, 
                                React.createElement("input", {className: 'form-control', name: 'filter', value: filter || '', onChange: this.onFilterChange, size: 30, maxLength: 80})
                            ))
                    ), 
                    React.createElement("div", {className: 'form-group'}, 
                        React.createElement("div", {className: 'col-sm-offset-2 col-sm-10'}, 
                            React.createElement("button", {type: 'button', onClick: this.submitSearchForm, className: 'btn btn-default'}, "Find Owner")
                        )
                    ))), 
            React.createElement(OwnersTable_1.default, {owners: owners}), 
            React.createElement(react_router_1.Link, {className: 'btn btn-default', to: '/owners/new'}, " Add Owner")));
    }
}
FindOwnersPage.contextTypes = {
    router: React.PropTypes.object.isRequired
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.default = FindOwnersPage;
;
//# sourceMappingURL=FindOwnersPage.js.map