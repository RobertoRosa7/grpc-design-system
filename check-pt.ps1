[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

$ptPattern = 'não|também|são|então|porque|através|além|isso|esse|essa|esses|essas|nosso|nossa|você|vocês|podemos|temos|fazemos|vamos|seria|serão|foram|eram|havia|houve|utilizado|utilizada|implementação|configuração|definição|geração|criação|validação|execução|operação|chamada|requisição|resposta|serviço|cliente|servidor|pedido|contrato|arquitetura|padrão|camada|adaptador|domínio|microsserviço|dados|campo|mensagem|método|classe|pacote|projeto|arquivo|após|próximo|próxima|seguir|seguinte|anterior|capítulo|seção|figura|tabela|exemplo|exercício|referência|código|compilar|compilação|compilado|basta|apenas|conforme|entrada|saída|retorno|parâmetro|variável|função|módulo|versão|formato|modelo|regra|lógica|negócio|processo|aplicação|componente|dependência|injeção|repositório|banco|coleção|índice|segurança|autenticação|autorização|criptografia|certificado|privada|pública|conexão|transporte|protocolo|serialização|binário|cabeçalho|metadado|interceptador|filtro|circuito|resiliência|observabilidade|rastreamento|métricas|alerta|monitoramento|container|orquestração|cluster|implantação|escalonamento|escalabilidade|disponibilidade|confiabilidade|latência|throughput|desempenho|eficiência|otimização|migração|integração|paralelo'

Get-ChildItem -Recurse -Filter "*.md" docs/ebook/manuscript/ | Sort-Object FullName | ForEach-Object {
    $file = $_.FullName
    $shortName = $file -replace '.*manuscript\\', ''
    $lines = [System.IO.File]::ReadAllLines($file, [System.Text.Encoding]::UTF8)
    $ptLines = @()
    for ($i = 0; $i -lt $lines.Count; $i++) {
        $line = $lines[$i]
        if ($line -match $ptPattern) {
            $ptLines += "  L$($i+1): $line"
        }
    }
    if ($ptLines.Count -gt 0) {
        Write-Output "=== $shortName ($($lines.Count) lines) ==="
        $ptLines | Select-Object -First 5 | ForEach-Object { Write-Output $_ }
        if ($ptLines.Count -gt 5) {
            Write-Output "  ... ($($ptLines.Count - 5) more)"
        }
        Write-Output ""
    } else {
        Write-Output "CLEAN: $shortName"
    }
}
