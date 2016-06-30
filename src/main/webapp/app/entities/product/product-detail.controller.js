(function() {
    'use strict';

    angular
        .module('computerWebstoreApp')
        .controller('ProductDetailController', ProductDetailController);

    ProductDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Product', 'Category'];

    function ProductDetailController($scope, $rootScope, $stateParams, entity, Product, Category) {
        var vm = this;

        vm.product = entity;

        var unsubscribe = $rootScope.$on('computerWebstoreApp:productUpdate', function(event, result) {
            vm.product = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
