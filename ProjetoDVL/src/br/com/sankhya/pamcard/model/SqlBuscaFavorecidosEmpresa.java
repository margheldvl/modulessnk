package br.com.sankhya.pamcard.model;

public class SqlBuscaFavorecidosEmpresa {
	
//	SELECT FAV.NUCIOT,
//    FAV.FAVORECIDO_TIPO,
//    FAV.FAVORECIDO_DOCUMENTO_QTDE,
//    FAV.FAVORECIDO_CPF_TIPO,
//    FAV.FAVORECIDO_CPF_NUMERO,
//    FAV.FAVORECIDO_RG_TIPO,
//    FAV.FAVORECIDO_RG_NUMERO,
//    FAV.FAVORECIDO_RG_UF,
//    FAV.FAVORECIDO_RG_EMISSOR_ID,
//    FAV.FAVORECIDO_RG_EMISSAO_DATA,
//    FAV.FAVORECIDO_RNTRC_TIPO,
//    FAV.FAVORECIDO_RNTRC_NUMERO,
//    FAV.FAVORECIDO_NOME,
//    FAV.FAVORECIDO_DATA_NASCIMENTO,
//    FAV.FAVORECIDO_NACIONALIDADE_ID,
//    FAV.FAVORECIDO_NATURALIDADE_IBGE,
//    FAV.FAVORECIDO_SEXO,
//    FAV.FAVORECIDO_ENDERECO_LOGRADOURO,
//    FAV.FAVORECIDO_ENDERECO_NUMERO,
//    FAV.FAVORECIDO_ENDERECO_BAIRRO,
//    FAV.FAVORECIDO_END_CIDADE_IBGE,
//    FAV.FAVORECIDO_ENDERECO_CEP,
//    FAV.FAVORECIDO_END_PROP_TIPO_ID,
//    FAV.FAVORECIDO_END_RESIDE_DESDE,
//    FAV.FAVORECIDO_TELEFONE_DDD,
//    FAV.FAVORECIDO_TELEFONE_NUMERO,
//    FAV.FAVORECIDO_MEIO_PAGTO,
//    FAV.FAVORECIDO_CARTAO_NUMERO,
//    FAV.FAVORECIDO_EMPRESA_NOME,
//    FAV.FAVORECIDO_EMPRESA_CNPJ,
//    FAV.FAVORECIDO_EMPRESA_RNTRC
//FROM   (
//    -- BUSCA DADOS DO CONTRATADO RESPONSÁVEL
//    SELECT CIOT.NUCIOT,
//           'viagem.favorecido1.tipo=1'                               AS
//           favorecido_tipo,
//           'viagem.favorecido1.documento.qtde=3'                     AS
//           favorecido_documento_qtde,
//           'viagem.favorecido1.documento1.tipo='
//           || CASE
//                WHEN PROP.TIPPESSOA = 'J' THEN 1
//                ELSE 2
//              END                                                    AS
//           favorecido_cpf_tipo,
//           'viagem.favorecido1.documento1.numero='
//           || PROP.CGC_CPF                                           AS
//           favorecido_cpf_numero,
//           'viagem.favorecido1.documento2.tipo='
//           || CASE
//                WHEN PROP.TIPPESSOA = 'J' THEN 4
//                ELSE 3
//              END                                                    AS
//           favorecido_rg_tipo,
//           'viagem.favorecido1.documento2.numero='
//           || NVL (PROP.IDENTINSCESTAD, 'ISENTA')                    AS
//           favorecido_rg_numero,
//           'viagem.favorecido1.documento2.uf='
//           || UFS.UF                                                 AS
//           favorecido_rg_uf,
//           'viagem.favorecido1.documento2.emissor.id= 1'             AS
//           favorecido_rg_emissor_id,
//           'viagem.favorecido1.documento2.emissao.data='
//           || NVL (TO_CHAR (FUN.DTRG, 'DD/MM/YYYY'), '01/01/1900')   AS
//           favorecido_rg_emissao_data,
//           'viagem.favorecido1.documento3.tipo=5'                    AS
//           favorecido_rntrc_tipo,
//           'viagem.favorecido1.documento3.numero='
//           || PROP.AD_RNTRC                                          AS
//           favorecido_rntrc_numero,
//           'viagem.favorecido1.nome='
//           || CASE
//                WHEN PROP.TIPPESSOA = 'J' THEN PROP.RAZAOSOCIAL
//                ELSE FUN.NOMEFUNC
//              END                                                    AS
//           favorecido_nome,
//           'viagem.favorecido1.data.nascimento='
//           || NVL (TO_CHAR (FUN.DTNASC, 'DD/MM/YYYY'), '01/01/1995') AS
//           favorecido_data_nascimento,
//           'viagem.favorecido1.nacionalidade.id=1'                   AS
//           favorecido_nacionalidade_id,
//           'viagem.favorecido1.naturalidade.ibge='
//           || CID.CODMUNFIS                                          AS
//           favorecido_naturalidade_ibge,
//           'viagem.favorecido1.sexo='
//           || NVL (FUN.SEXO, 'M')                                    AS
//           favorecido_sexo,
//           'viagem.favorecido1.endereco.logradouro='
//           || END.NOMEEND                                            AS
//           favorecido_endereco_logradouro,
//           'viagem.favorecido1.endereco.numero='
//           || PROP.NUMEND                                            AS
//           favorecido_endereco_numero,
//           'viagem.favorecido1.endereco.bairro='
//           || RET_END_CLIENTE (PROP.CODPARC, 'BAI')                  AS
//           favorecido_endereco_bairro,
//           'viagem.favorecido1.endereco.cidade.ibge='
//           || CID.CODMUNFIS                                          AS
//           favorecido_END_cidade_ibge,
//           'viagem.favorecido1.endereco.cep='
//           || PROP.CEP                                               AS
//           favorecido_endereco_cep,
//           'viagem.favorecido1.endereco.propriedade.tipo.id=1'       AS
//           favorecido_END_prop_tipo_id,
//           'viagem.favorecido1.endereco.reside.desde='
//           || TO_CHAR (PROP.DTCAD, 'MM/YYYY')                        AS
//           favorecido_END_reside_desde,
//           'viagem.favorecido1.telefone.ddd='
//           || '0'
//           || SUBSTR (PROP.TELEFONE, 1, 2)                           AS
//           favorecido_telefone_ddd,
//           'viagem.favorecido1.telefone.numero='
//           || SUBSTR (FORMATATELEFONE (PROP.TELEFONE), 5, INSTR (
//                 FORMATATELEFONE (PROP.TELEFONE), '-') - 5)
//           || SUBSTR (FORMATATELEFONE (PROP.TELEFONE),
//                 INSTR (FORMATATELEFONE (PROP.TELEFONE), '-')
//                                                       + 1, 4)       AS
//           favorecido_telefone_numero,
//           'viagem.favorecido1.meio.pagamento=1'                     AS
//           FAVORECIDO_MEIO_PAGTO,
//           'viagem.favorecido1.cartao.numero='
//           || PROP.AD_CARTAOPAMCARD                                  AS
//           favorecido_cartao_numero,
//           CASE
//             WHEN PROP.TIPPESSOA = 'J' THEN
//             'viagem.favorecido1.empresa.nome='
//             || PROP.RAZAOSOCIAL
//             ELSE ''
//           END                                                       AS
//           favorecido_empresa_nome,
//           CASE
//             WHEN PROP.TIPPESSOA = 'J' THEN
//             'viagem.favorecido1.empresa.cnpj='
//             || PROP.CGC_CPF
//             ELSE ''
//           END                                                       AS
//           favorecido_empresa_cnpj,
//           CASE
//             WHEN PROP.TIPPESSOA = 'J' THEN
//             'viagem.favorecido1.empresa.rntrc='
//             || RTRIM (PROP.AD_RNTRC)
//             ELSE ''
//           END                                                       AS
//           favorecido_empresa_rntrc
//    FROM   AD_TOLCIOT CIOT
//           INNER JOIN TGFPAR PROP
//                   ON CIOT.CODPROPRIETARIO = PROP.CODPARC
//           INNER JOIN TSICID CID
//                   ON PROP.CODCID = CID.CODCID
//           INNER JOIN TSIEND END
//                   ON PROP.CODEND = END.CODEND
//           INNER JOIN TSIUFS UFS
//                   ON CID.UF = UFS.CODUF
//           INNER JOIN TGFVEI VEI
//                   ON CIOT.CODVEICULO = VEI.CODVEICULO
//           LEFT JOIN TFPFUN FUN
//                  ON PROP.CODPARC = FUN.CODPARC
//                     AND ( FUN.CODEMP = 1
//                            OR FUN.CODEMP = 101 )
//    WHERE  PROP.TIPPESSOA = 'F'
//           AND ( CIOT.CODMOTORISTA = CIOT.CODPROPRIETARIO )
//    UNION ALL
//    SELECT CIOT.NUCIOT,
//           'viagem.favorecido1.tipo=1'                               AS
//           favorecido_tipo,
//           'viagem.favorecido1.documento.qtde=3'                     AS
//           favorecido_documento_qtde,
//           'viagem.favorecido1.documento1.tipo='
//           || CASE
//                WHEN PROP.TIPPESSOA = 'J' THEN 1
//                ELSE 2
//              END                                                    AS
//           favorecido_cpf_tipo,
//           'viagem.favorecido1.documento1.numero='
//           || PROP.CGC_CPF                                           AS
//           favorecido_cpf_numero,
//           'viagem.favorecido1.documento2.tipo='
//           || CASE
//                WHEN PROP.TIPPESSOA = 'J' THEN 4
//                ELSE 3
//              END                                                    AS
//           favorecido_rg_tipo,
//           'viagem.favorecido1.documento2.numero='
//           || NVL (PROP.IDENTINSCESTAD, 'ISENTA')                    AS
//           favorecido_rg_numero,
//           'viagem.favorecido1.documento2.uf='
//           || UFS.UF                                                 AS
//           favorecido_rg_uf,
//           'viagem.favorecido1.documento2.emissor.id= 1'             AS
//           favorecido_rg_emissor_id,
//           'viagem.favorecido1.documento2.emissao.data='
//           || NVL (TO_CHAR (FUN.DTRG, 'DD/MM/YYYY'), '01/01/1900')   AS
//           favorecido_rg_emissao_data,
//           'viagem.favorecido1.documento3.tipo=5'                    AS
//           favorecido_rntrc_tipo,
//           'viagem.favorecido1.documento3.numero='
//           || PROP.AD_RNTRC                                          AS
//           favorecido_rntrc_numero,
//           'viagem.favorecido1.nome='
//           || CASE
//                WHEN PROP.TIPPESSOA = 'J' THEN PROP.RAZAOSOCIAL
//                ELSE FUN.NOMEFUNC
//              END                                                    AS
//           favorecido_nome,
//           'viagem.favorecido1.data.nascimento='
//           || NVL (TO_CHAR (FUN.DTNASC, 'DD/MM/YYYY'), '01/01/1995') AS
//           favorecido_data_nascimento,
//           'viagem.favorecido1.nacionalidade.id=1'                   AS
//           favorecido_nacionalidade_id,
//           'viagem.favorecido1.naturalidade.ibge='
//           || CID.CODMUNFIS                                          AS
//           favorecido_naturalidade_ibge,
//           'viagem.favorecido1.sexo='
//           || NVL (FUN.SEXO, 'M')                                    AS
//           favorecido_sexo,
//           'viagem.favorecido1.endereco.logradouro='
//           || END.NOMEEND                                            AS
//           favorecido_endereco_logradouro,
//           'viagem.favorecido1.endereco.numero='
//           || PROP.NUMEND                                            AS
//           favorecido_endereco_numero,
//           'viagem.favorecido1.endereco.bairro='
//           || RET_END_CLIENTE (PROP.CODPARC, 'BAI')                  AS
//           favorecido_endereco_bairro,
//           'viagem.favorecido1.endereco.cidade.ibge='
//           || CID.CODMUNFIS                                          AS
//           favorecido_END_cidade_ibge,
//           'viagem.favorecido1.endereco.cep='
//           || PROP.CEP                                               AS
//           favorecido_endereco_cep,
//           'viagem.favorecido1.endereco.propriedade.tipo.id=1'       AS
//           favorecido_END_prop_tipo_id,
//           'viagem.favorecido1.endereco.reside.desde='
//           || TO_CHAR (PROP.DTCAD, 'MM/YYYY')                        AS
//           favorecido_END_reside_desde,
//           'viagem.favorecido1.telefone.ddd='
//           || '0'
//           || SUBSTR (PROP.TELEFONE, 1, 2)                           AS
//           favorecido_telefone_ddd,
//           'viagem.favorecido1.telefone.numero='
//           || SUBSTR (FORMATATELEFONE (PROP.TELEFONE), 5, INSTR (
//                 FORMATATELEFONE (PROP.TELEFONE), '-') - 5)
//           || SUBSTR (FORMATATELEFONE (PROP.TELEFONE),
//                 INSTR (FORMATATELEFONE (PROP.TELEFONE), '-')
//                                                       + 1, 4)       AS
//           favorecido_telefone_numero,
//           'viagem.favorecido1.meio.pagamento=1'                     AS
//           FAVORECIDO_MEIO_PAGTO,
//           'viagem.favorecido1.cartao.numero='
//           || PROP.AD_CARTAOPAMCARD                                  AS
//           favorecido_cartao_numero,
//           CASE
//             WHEN PROP.TIPPESSOA = 'J' THEN
//             'viagem.favorecido1.empresa.nome='
//             || PROP.RAZAOSOCIAL
//             ELSE ''
//           END                                                       AS
//           favorecido_empresa_nome,
//           CASE
//             WHEN PROP.TIPPESSOA = 'J' THEN
//             'viagem.favorecido1.empresa.cnpj='
//             || PROP.CGC_CPF
//             ELSE ''
//           END                                                       AS
//           favorecido_empresa_cnpj,
//           CASE
//             WHEN PROP.TIPPESSOA = 'J' THEN
//             'viagem.favorecido1.empresa.rntrc='
//             || RTRIM (PROP.AD_RNTRC)
//             ELSE ''
//           END                                                       AS
//           favorecido_empresa_rntrc
//    FROM   AD_TOLCIOT CIOT
//           INNER JOIN TGFPAR PROP
//                   ON CIOT.CODPROPRIETARIO = PROP.CODPARC
//           INNER JOIN TSICID CID
//                   ON PROP.CODCID = CID.CODCID
//           INNER JOIN TSIEND END
//                   ON PROP.CODEND = END.CODEND
//           INNER JOIN TSIUFS UFS
//                   ON CID.UF = UFS.CODUF
//           INNER JOIN TGFVEI VEI
//                   ON CIOT.CODVEICULO = VEI.CODVEICULO
//           LEFT JOIN TFPFUN FUN
//                  ON PROP.CODPARC = FUN.CODPARC
//                     AND ( FUN.CODEMP = 1
//                            OR FUN.CODEMP = 101 )
//    WHERE  PROP.TIPPESSOA = 'J'
//     UNION ALL
//     SELECT CIOT.NUCIOT,
//            'viagem.favorecido'
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '21')
//            || '.tipo='
//            || CASE
//                 WHEN PROP.TIPPESSOA = 'J' THEN 1
//                 ELSE 3
//               END                                                  AS
//            favorecido_tipo,
//            'viagem.favorecido'
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '21')
//            || '.documento.qtde='
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '23')               AS
//            favorecido_documento_qtde,
//            'viagem.favorecido'
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '21')
//            || '.documento1.tipo=2'                                 AS
//            favorecido_cpf_tipo,
//            'viagem.favorecido'
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '21')
//            || '.documento1.numero='
//            || MOT.CGC_CPF                                          AS
//            favorecido_cpf_numero,
//            'viagem.favorecido'
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '21')
//            || '.documento2.tipo=3'                                 AS
//            favorecido_rg_tipo,
//            'viagem.favorecido'
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '21')
//            || '.documento2.numero='
//            || MOT.IDENTINSCESTAD                                   AS
//            favorecido_rg_numero,
//            'viagem.favorecido'
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '21')
//            || '.documento2.uf='
//            || UFS.UF                                               AS
//            favorecido_rg_uf,
//            'viagem.favorecido'
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '21')
//            || '.documento2.emissor.id=1'                           AS
//            favorecido_rg_emissor_id,
//            'viagem.favorecido'
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '21')
//            || '.documento2.emissao.data='
//            || NVL (TO_CHAR (FUN.DTRG, 'DD/MM/YYYY'), '01/01/1900') AS
//            favorecido_rg_emissao_data,
//            CASE
//              WHEN PROP.TIPPESSOA = 'J' THEN 'viagem.favorecido'
//                                             ||
//              TRANSLATE (PROP.TIPPESSOA, 'FJ', '21'
//              )
//                                             || '.documento3.tipo=5'
//              ELSE NULL
//            END                                                     AS
//            favorecido_rntrc_tipo,
//            CASE
//              WHEN PROP.TIPPESSOA = 'J' THEN 'viagem.favorecido'
//                                             ||
//              TRANSLATE (PROP.TIPPESSOA, 'FJ', '21'
//              )
//                                             || '.documento3.numero='
//                                             || MOT.AD_RNTRC
//              ELSE NULL
//            END                                                     AS
//            favorecido_rntrc_numero,
//            'viagem.favorecido'
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '21')
//            || '.nome='
//            || FUN.NOMEFUNC                                         AS
//            favorecido_nome,
//            'viagem.favorecido'
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '21')
//            || '.data.nascimento='
//            || TO_CHAR (FUN.DTNASC, 'DD/MM/YYYY')                   AS
//            favorecido_data_nascimento,
//            'viagem.favorecido'
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '21')
//            || '.nacionalidade.id=1'                                AS
//            favorecido_nacionalidade_id,
//            'viagem.favorecido'
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '21')
//            || '.naturalidade.ibge='
//            || CID.CODMUNFIS                                        AS
//            favorecido_naturalidade_ibge,
//            'viagem.favorecido'
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '21')
//            || '.sexo='
//            || FUN.SEXO                                             AS
//            favorecido_sexo,
//            'viagem.favorecido'
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '21')
//            || '.endereco.logradouro='
//            || END.NOMEEND                                          AS
//            favorecido_endereco_logradouro,
//            'viagem.favorecido'
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '21')
//            || '.endereco.numero='
//            || MOT.NUMEND                                           AS
//            favorecido_endereco_numero,
//            'viagem.favorecido'
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '21')
//            || '.endereco.bairro='
//            || RET_END_CLIENTE (MOT.CODPARC, 'BAI')                 AS
//            favorecido_endereco_bairro,
//            'viagem.favorecido'
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '21')
//            || '.endereco.cidade.ibge='
//            || CID.CODMUNFIS                                        AS
//            favorecido_END_cidade_ibge,
//            'viagem.favorecido'
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '21')
//            || '.endereco.cep='
//            || MOT.CEP                                              AS
//            favorecido_endereco_cep,
//            'viagem.favorecido'
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '21')
//            || '.endereco.propriedade.tipo.id=1'                    AS
//            favorecido_END_MOT_tipo_id,
//            'viagem.favorecido'
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '21')
//            || '.endereco.reside.desde='
//            || TO_CHAR (MOT.DTCAD, 'MM/YYYY')                       AS
//            favorecido_END_reside_desde,
//            'viagem.favorecido'
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '21')
//            || '.telefone.ddd='
//            || '0'
//            || SUBSTR (MOT.TELEFONE, 1, 2)                          AS
//            favorecido_telefone_ddd,
//            'viagem.favorecido'
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '21')
//            || '.telefone.numero='
//            || SUBSTR (FORMATATELEFONE (MOT.TELEFONE), 5,
//                  INSTR (FORMATATELEFONE (MOT.TELEFONE), '-') - 5)
//            || SUBSTR (FORMATATELEFONE (MOT.TELEFONE),
//                  INSTR (FORMATATELEFONE (MOT.TELEFONE), '-')
//                                                       + 1, 4)      AS
//            favorecido_telefone_numero,
//            'viagem.favorecido'
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '21')
//            || '.meio.pagamento=1'                                  AS
//            FAVORECIDO_MEIO_PAGTO,
//            'viagem.favorecido'
//            || TRANSLATE (PROP.TIPPESSOA, 'FJ', '21')
//            || '.cartao.numero='
//            || MOT.AD_CARTAOPAMCARD                                 AS
//            favorecido_cartao_numero,
//            CASE
//              WHEN PROP.TIPPESSOA = 'J' THEN 'viagem.favorecido'
//                                             ||
//              TRANSLATE (PROP.TIPPESSOA, 'FJ', '21'
//              )
//                                             || '.empresa.nome='
//                                             || REMOVEESPACO (REMOVE_ACENTOS
//(PROP.RAZAOSOCIAL))
//ELSE NULL
//END                                                     AS
//favorecido_empresa_nome,
//CASE
//WHEN PROP.TIPPESSOA = 'J' THEN 'viagem.favorecido'
//||
//TRANSLATE (PROP.TIPPESSOA, 'FJ', '21'
//)
//|| '.empresa.cnpj='
//|| PROP.CGC_CPF
//ELSE NULL
//END                                                     AS
//favorecido_empresa_cnpj,
//CASE
//WHEN PROP.TIPPESSOA = 'J' THEN 'viagem.favorecido'
//||
//TRANSLATE (PROP.TIPPESSOA, 'FJ', '21'
//)
//|| '.empresa.rntrc='
//|| PROP.AD_RNTRC
//ELSE NULL
//END                                                     AS
//favorecido_empresa_rntrc
//FROM   AD_TOLCIOT CIOT
//INNER JOIN TGFPAR MOT
//ON CIOT.CODMOTORISTA = MOT.CODPARC
//INNER JOIN TGFPAR PROP
//--   ON CIOT.CODPROPRIETARIO = PROP.CODPARC
//ON ( ( CIOT.CODPROPRIETARIO = PROP.CODPARC
//AND CIOT.CODMOTORISTA = PROP.CODPARC )
//OR ( CIOT.CODPROPRIETARIO <> PROP.CODPARC
//AND CIOT.CODMOTORISTA = PROP.CODPARC ) )
//INNER JOIN TSICID CID
//ON MOT.CODCID = CID.CODCID
//LEFT JOIN TFPFUN FUN
//ON MOT.CODPARC = FUN.CODPARC
//AND ( FUN.CODEMP = 1
//OR FUN.CODEMP = 101 )
//INNER JOIN TSIEND END
//ON MOT.CODEND = END.CODEND
//INNER JOIN TSIUFS UFS
//ON CID.UF = UFS.CODUF
//INNER JOIN TGFVEI VEI
//ON CIOT.CODVEICULO = VEI.CODVEICULO
//LEFT JOIN TGFPAR PROPEMP
//ON CIOT.CODPROPRIETARIO = PROPEMP.CODPARC) FAV
//WHERE  NUCIOT = 5950
//     OR NUCIOT = 8457 

}
