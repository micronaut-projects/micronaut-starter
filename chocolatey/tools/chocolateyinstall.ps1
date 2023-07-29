$version = '4.0.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'B2A0F1DC96BBB8A8ED13608E16C9A134B6857471E067530952CB3D707432FA72'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
