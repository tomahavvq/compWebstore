(function() {
    'use strict';
    angular
        .module('computerWebstoreApp')
        .factory('ProductDetails', ProductDetails);

    ProductDetails.$inject = ['$resource'];

    function ProductDetails ($resource) {
        var resourceUrl =  'api/product-details/:id';

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
