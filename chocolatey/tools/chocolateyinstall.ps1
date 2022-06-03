$version = '3.5.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '6B99C33A44704B2235C42371CBEA81401281878A94E632A08B0768B7D1622F42'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
