(function() {
    'use strict';

    angular
        .module('computerWebstoreApp')
        .controller('ProductDetailsDeleteController',ProductDetailsDeleteController);

    ProductDetailsDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProductDetails'];

    function ProductDetailsDeleteController($uibModalInstance, entity, ProductDetails) {
        var vm = this;

        vm.productDetails = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProductDetails.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
