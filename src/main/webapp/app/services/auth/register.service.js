(function () {
    'use strict';

    angular
        .module('computerWebstoreApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
