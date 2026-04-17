$version = '4.10.12'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'A37A38863AC8C4A69849679EEEFD2B9FF9FB97046A9EA9A08F981E17DC27EC22'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
