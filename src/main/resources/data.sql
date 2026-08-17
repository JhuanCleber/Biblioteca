
INSERT INTO jogo (nome_fase, dificuldade, tipo_operacao, assets_url, id_administrador)
SELECT 'Soma Mágica', 'facil', 'soma', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM jogo WHERE nome_fase = 'Soma Mágica');
 
INSERT INTO jogo (nome_fase, dificuldade, tipo_operacao, assets_url, id_administrador)
SELECT 'Subtração Espacial', 'facil', 'subtracao', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM jogo WHERE nome_fase = 'Subtração Espacial');
 
INSERT INTO jogo (nome_fase, dificuldade, tipo_operacao, assets_url, id_administrador)
SELECT 'Multiplicação Maluca', 'medio', 'multiplicacao', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM jogo WHERE nome_fase = 'Multiplicação Maluca');
 
INSERT INTO jogo (nome_fase, dificuldade, tipo_operacao, assets_url, id_administrador)
SELECT 'Divisão Divertida', 'medio', 'divisao', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM jogo WHERE nome_fase = 'Divisão Divertida');
 
INSERT INTO jogo (nome_fase, dificuldade, tipo_operacao, assets_url, id_administrador)
SELECT 'Formas Geométricas', 'facil', 'geometria', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM jogo WHERE nome_fase = 'Formas Geométricas');
 
INSERT INTO jogo (nome_fase, dificuldade, tipo_operacao, assets_url, id_administrador)
SELECT 'Contando até 100', 'facil', 'contagem', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM jogo WHERE nome_fase = 'Contando até 100');

INSERT INTO jogo (nome_fase, dificuldade, tipo_operacao, assets_url, id_administrador)
SELECT 'Sequência Secreta', 'facil', 'sequencia', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM jogo WHERE nome_fase = 'Sequência Secreta');

INSERT INTO jogo (nome_fase, dificuldade, tipo_operacao, assets_url, id_administrador)
SELECT 'Símbolos Mágicos', 'facil', 'comparacao', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM jogo WHERE nome_fase = 'Símbolos Mágicos');

INSERT INTO jogo (nome_fase, dificuldade, tipo_operacao, assets_url, id_administrador)
SELECT 'Hora da Aventura', 'facil', 'horas', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM jogo WHERE nome_fase = 'Hora da Aventura');

INSERT INTO jogo (nome_fase, dificuldade, tipo_operacao, assets_url, id_administrador)
SELECT 'Cofrinho Mágico', 'medio', 'dinheiro', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM jogo WHERE nome_fase = 'Cofrinho Mágico');

INSERT INTO jogo (nome_fase, dificuldade, tipo_operacao, assets_url, id_administrador)
SELECT 'Frações Coloridas', 'medio', 'fracoes', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM jogo WHERE nome_fase = 'Frações Coloridas');
