(function() {
    'use strict';

    angular
        .module('computerWebstoreApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('store-order', {
            parent: 'entity',
            url: '/store-order',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'StoreOrders'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/store-order/store-orders.html',
                    controller: 'StoreOrderController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('store-order-detail', {
            parent: 'entity',
            url: '/store-order/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'StoreOrder'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/store-order/store-order-detail.html',
                    controller: 'StoreOrderDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'StoreOrder', function($stateParams, StoreOrder) {
                    return StoreOrder.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('store-order.new', {
            parent: 'store-order',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/store-order/store-order-dialog.html',
                    controller: 'StoreOrderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                productAmount: null,
                                totalSum: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('store-order', null, { reload: true });
                }, function() {
                    $state.go('store-order');
                });
            }]
        })
        .state('store-order.edit', {
            parent: 'store-order',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/store-order/store-order-dialog.html',
                    controller: 'StoreOrderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StoreOrder', function(StoreOrder) {
                            return StoreOrder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('store-order', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('store-order.delete', {
            parent: 'store-order',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/store-order/store-order-delete-dialog.html',
                    controller: 'StoreOrderDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StoreOrder', function(StoreOrder) {
                            return StoreOrder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('store-order', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
