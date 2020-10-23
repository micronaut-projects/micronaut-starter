$version = '2.1.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '1B37D3E61C03DC8C7CB1F61AF409FA958A97F6D6D4AE1C7CC7D9DEE032E283F1'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
