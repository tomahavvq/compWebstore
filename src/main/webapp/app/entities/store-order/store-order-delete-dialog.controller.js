(function() {
    'use strict';

    angular
        .module('computerWebstoreApp')
        .controller('StoreOrderDeleteController',StoreOrderDeleteController);

    StoreOrderDeleteController.$inject = ['$uibModalInstance', 'entity', 'StoreOrder'];

    function StoreOrderDeleteController($uibModalInstance, entity, StoreOrder) {
        var vm = this;

        vm.storeOrder = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StoreOrder.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
