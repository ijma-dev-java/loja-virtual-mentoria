package br.com.loja.virtual.mentoria;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.loja.virtual.mentoria.enums.ApiTokenIntegracao;
import br.com.loja.virtual.mentoria.model.dto.EmpresaTransporteDTO;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MelhorEnvioAPITest {
	
	public static void main(String[] args) throws Exception {
		
		// Instancia do objeto de Requisição
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		
		// Tipo de dados em JSON
		MediaType mediaType = MediaType.parse("application/json");
		
		// JSON do objeto responsável pelo cálculo de frete de produto
		RequestBody body = RequestBody.create(mediaType, "{ \"from\": { \"postal_code\": \"53330201\" }, \"to\": { \"postal_code\": \"54350330\" }, \"products\": [ { \"id\": \"x\", \"width\": 11, \"height\": 17, \"length\": 11, \"weight\": 0.3, \"insurance_value\": 10.1, \"quantity\": 1 }, { \"id\": \"y\", \"width\": 16, \"height\": 25, \"length\": 11, \"weight\": 0.3, \"insurance_value\": 55.05, \"quantity\": 2 }, { \"id\": \"z\", \"width\": 22, \"height\": 30, \"length\": 11, \"weight\": 1, \"insurance_value\": 30, \"quantity\": 1 } ] }");
		
		// Instanciando o Request e fazendo o serviço
		Request request = new Request.Builder()
				  .url(ApiTokenIntegracao.URL_MELHOR_ENVIO_SAND_BOX +"api/v2/me/shipment/calculate")
				  .method("POST", body)
				  .addHeader("Accept", "application/json")
				  .addHeader("Content-Type", "application/json")
				  .addHeader("Authorization", "Bearer " + ApiTokenIntegracao.TOKEN_MELHOR_ENVIO_SAND_BOX)
				  .addHeader("User-Agent", "ijma.services.tech@gmail.com")
				  .build();
		
		// Executando a requisição e dando a resposta
		Response response = client.newCall(request).execute();
		
		// Fazendo a leitura da resposta
		JsonNode jsonNode = new ObjectMapper().readTree(response.body().string());
		
		// Atribuindo a leitura ao Iterator do java.util 
		Iterator<JsonNode> iterator = jsonNode.iterator();
		
		// Instanciando numa lista o EmpresaTransporteDTO
		List<EmpresaTransporteDTO> empresaTransporteDTOs = new ArrayList<EmpresaTransporteDTO>();
		
		// Percorrendo
		while (iterator.hasNext()) {
			
			// Pegando um elemento de cada linha
			JsonNode node = iterator.next();
			
			// Instanciando o EmpresaTransporteDTO
			EmpresaTransporteDTO empresaTransporteDTO = new EmpresaTransporteDTO();
			
			// Verificando o id
			if (node.get("id") != null) {
				
				// Seta apenas o texto
				empresaTransporteDTO.setId(node.get("id").asText());
				
			}
			
			// Verificando o nome
			if (node.get("name") != null) {
				
				// Seta apenas o texto
				empresaTransporteDTO.setNome(node.get("name").asText());
				
			}
			
			// Verificando o preço
			if (node.get("price") != null) {
				
				// Seta apenas o texto
				empresaTransporteDTO.setValor(node.get("price").asText());
				
			}
			
			// Verificando a empreas
			if (node.get("company") != null) {
				
				// Seta apenas o texto
				empresaTransporteDTO.setEmpresa(node.get("company").get("name").asText());
				empresaTransporteDTO.setPicture(node.get("company").get("picture").asText());
				
			}
			
			// Verificando os dados
			if (empresaTransporteDTO.dadosOK()) {
				
				// Seta apenas o texto
				empresaTransporteDTOs.add(empresaTransporteDTO);
				
			}

		}
		
		System.out.println(empresaTransporteDTOs);
		
	}

}
