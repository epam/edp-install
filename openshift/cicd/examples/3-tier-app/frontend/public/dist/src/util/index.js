"use strict";
const BACKEND_URL = (typeof __API_SERVER_URL__ === 'undefined' ? '' : __API_SERVER_URL__);
exports.url = (path) => `${BACKEND_URL}/${path}`;
exports.submitForm = (method, path, data, onSuccess) => {
    const requestUrl = exports.url(path);
    const fetchParams = {
        method: method,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    };
    console.log('Submitting to ' + method + ' ' + requestUrl);
    return fetch(requestUrl, fetchParams)
        .then(response => response.status === 204 ? onSuccess(response.status, {}) : response.json().then(result => onSuccess(response.status, result)));
};
//# sourceMappingURL=index.js.map