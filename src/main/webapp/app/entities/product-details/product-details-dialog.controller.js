(function() {
    'use strict';

    angular
        .module('computerWebstoreApp')
        .controller('ProductDetailsDialogController', ProductDetailsDialogController);

    ProductDetailsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'ProductDetails', 'Product'];

    function ProductDetailsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, ProductDetails, Product) {
        var vm = this;

        vm.productDetails = entity;
        vm.clear = clear;
        vm.save = save;
        vm.products = Product.query({filter: 'productdetails-is-null'});
        $q.all([vm.productDetails.$promise, vm.products.$promise]).then(function() {
            if (!vm.productDetails.productId) {
                return $q.reject();
            }
            return Product.get({id : vm.productDetails.productId}).$promise;
        }).then(function(product) {
            vm.products.push(product);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.productDetails.id !== null) {
                ProductDetails.update(vm.productDetails, onSaveSuccess, onSaveError);
            } else {
                ProductDetails.save(vm.productDetails, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('computerWebstoreApp:productDetailsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
