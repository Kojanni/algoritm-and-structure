# Скрипт для автоматического сжатия/распаковки и сбора метрик

$testDir = "C:\Users\user\IdeaProjects\otus\algoritm-and-structure\src\test\java\org\micro\kojanni\data_compression_algorithms"
$classPath = "C:\Users\user\IdeaProjects\otus\algoritm-and-structure\target\classes"
$toolClass = "org.micro.kojanni.data_compression_algorithms.rle.RLETool"

# Тестовые файлы
$files = @(
    @{Name="input.txt"; Type="текст"},
    @{Name="pic.jpg"; Type="изображение"},
    @{Name="Korol_i_SHut_-_Lesnik.mp3"; Type="аудио"},
    @{Name="Utorrent182.zip"; Type="архив"}
)

$algorithms = @("--basic", "--improved")

$results = @()

foreach ($file in $files) {
    $inputPath = Join-Path $testDir $file.Name
    $originalSize = (Get-Item $inputPath).Length
    
    foreach ($algo in $algorithms) {
        $algoName = if ($algo -eq "--basic") { "BASIC" } else { "PACKBITS" }
        $compressedPath = Join-Path $testDir "$($file.Name).$algoName.rle"
        $decompressedPath = Join-Path $testDir "$($file.Name).$algoName.restored"
        
        # Сжатие
        $compressTime = Measure-Command {
            java -cp $classPath $toolClass compress $inputPath $compressedPath $algo 2>&1 | Out-Null
        }
        
        if (Test-Path $compressedPath) {
            $compressedSize = (Get-Item $compressedPath).Length
            $ratio = [math]::Round($compressedSize / $originalSize, 4)
            $delta = $originalSize - $compressedSize
            
            # Распаковка
            $decompressTime = Measure-Command {
                java -cp $classPath $toolClass decompress $compressedPath $decompressedPath $algo 2>&1 | Out-Null
            }
            
            # Контрольные суммы
            $originalHash = (certutil -hashfile $inputPath SHA256 | Select-String -Pattern "^[a-f0-9]{64}$").ToString().Trim()
            $restoredHash = if (Test-Path $decompressedPath) {
                (certutil -hashfile $decompressedPath SHA256 | Select-String -Pattern "^[a-f0-9]{64}$").ToString().Trim()
            } else {
                "N/A"
            }
            
            $results += [PSCustomObject]@{
                Type = $file.Type
                Algorithm = $algoName
                OriginalSize = $originalSize
                CompressedSize = $compressedSize
                Ratio = $ratio
                Delta = $delta
                CompressTime = [math]::Round($compressTime.TotalMilliseconds, 2)
                DecompressTime = [math]::Round($decompressTime.TotalMilliseconds, 2)
                OriginalHash = $originalHash
                RestoredHash = $restoredHash
                Match = if ($originalHash -eq $restoredHash) { "OK" } else { "FAIL" }
            }
        }
    }
}

# Вывод результатов
$results | Format-Table -AutoSize

# Сохранение в CSV
$csvPath = Join-Path $testDir "test_results.csv"
$results | Export-Csv -Path $csvPath -NoTypeInformation -Encoding UTF8
Write-Host "Результаты сохранены в: $csvPath"
