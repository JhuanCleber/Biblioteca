
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
