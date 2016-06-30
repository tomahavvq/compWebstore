(function() {
    'use strict';
    angular
        .module('computerWebstoreApp')
        .factory('StoreOrder', StoreOrder);

    StoreOrder.$inject = ['$resource'];

    function StoreOrder ($resource) {
        var resourceUrl =  'api/store-orders/:id';

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
