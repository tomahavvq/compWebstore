(function() {
    'use strict';

    angular
        .module('computerWebstoreApp')
        .controller('ProductDetailsController', ProductDetailsController);

    ProductDetailsController.$inject = ['$scope', '$state', 'ProductDetails'];

    function ProductDetailsController ($scope, $state, ProductDetails) {
        var vm = this;
        
        vm.productDetails = [];

        loadAll();

        function loadAll() {
            ProductDetails.query(function(result) {
                vm.productDetails = result;
            });
        }
    }
})();
