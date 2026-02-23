-- Asegúrate de que EnumTipoFuenteProxy se mapea a un VARCHAR o similar en la DB.
-- Los valores deben coincidir con la definición de tu enum en Java.

INSERT INTO fuente (url, tipo_fuente_proxy) VALUES
('https://ejemplo.com/rss1', 'METAMAPA'),
('https://noticias.com/api', 'DEMO');