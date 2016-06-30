(function() {
    'use strict';

    angular
        .module('computerWebstoreApp')
        .controller('ProductDetailsDetailController', ProductDetailsDetailController);

    ProductDetailsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ProductDetails', 'Product'];

    function ProductDetailsDetailController($scope, $rootScope, $stateParams, entity, ProductDetails, Product) {
        var vm = this;

        vm.productDetails = entity;

        var unsubscribe = $rootScope.$on('computerWebstoreApp:productDetailsUpdate', function(event, result) {
            vm.productDetails = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
