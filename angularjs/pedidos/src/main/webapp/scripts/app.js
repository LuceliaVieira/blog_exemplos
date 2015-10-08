(function() {
  angular.module("app", []);

  angular.module("app")
    .factory("BalcaoFactory", function($http, $timeout) {
      var promise;
      var aberto = false;
      var pedidos = [];
      var URL = "http://localhost:8080/pedidos/rest/lanchonete";

      return {
        isAberto: isAberto,
        getPedidos: getPedidos,
        iniciarAtendimento: iniciarAtendimento,
        encerrarAtendimento: encerrarAtendimento,
        atenderPedido: atenderPedido
      };

      function isAberto() {
        return aberto;
      }

      function getPedidos() {
        return pedidos;
      }

      function atualizarPedidos() {
        $http.get(URL+"/pedidos")
          .success(function(data) {
            pedidos = data;
          });
      }

      function ativarRealTime() {
        atualizarPedidos();
        promise = $timeout(ativarRealTime, 9000);
      }

      function iniciarAtendimento() {
        $http.post(URL+"/iniciarAtendimento", {});
        aberto = true;
        ativarRealTime();
      }

      function encerrarAtendimento() {
        aberto = false;
        $http.post(URL+"/encerrarAtendimento", {});
        $timeout.cancel(promise);
      }

      function atenderPedido(pedido) {
    	  pedido.emAtendimento = true;
        $http.delete(URL+"/pedidos/" + pedido.id);
      }
    });

  angular.module("app")
    .controller("BalcaoController", function(BalcaoFactory) {
      var self = this;
      self.fn = BalcaoFactory;
    });

})();