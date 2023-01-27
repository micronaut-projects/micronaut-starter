$version = '3.5.6'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '51A57613C7FD1E449DB013D00100A14AE7DFB3E47F32533B78B49C410AEB9956'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
