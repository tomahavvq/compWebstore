(function() {
    'use strict';
    angular
        .module('computerWebstoreApp')
        .factory('OrderDetails', OrderDetails);

    OrderDetails.$inject = ['$resource'];

    function OrderDetails ($resource) {
        var resourceUrl =  'api/order-details/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
